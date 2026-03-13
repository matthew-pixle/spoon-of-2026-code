package frc.robot.subsystems.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.List;

public class Leds extends SubsystemBase {

  private static final Leds instance = new Leds();

  public static Leds getInstance() {
    return instance;
  }
//creates leds and buffer
  private final AddressableLED leds = new AddressableLED(LedConstants.kPort);
  private final AddressableLEDBuffer buffer = new AddressableLEDBuffer(LedConstants.kFullLength);
//starts leds section
  public record Section(int start, int end) {}
//starts enums
  public enum LedSection {
    ALL(new Section(0, LedConstants.kFullLength - 1)),
    ALL_LEFT(
        new Section(
            0, LedConstants.kLeftTurretBottomLength + LedConstants.kLeftTurretTopLength - 1)),
    ALL_RIGHT(
        new Section(
            LedConstants.kLeftTurretBottomLength + LedConstants.kLeftTurretTopLength,
            LedConstants.kFullLength)),
    BOTTOM_LEFT_TURRET(new Section(0, LedConstants.kLeftTurretBottomLength - 1)),
    TOP_LEFT_TURRET(
        new Section(
            LedConstants.kLeftTurretBottomLength,
            LedConstants.kLeftTurretBottomLength + LedConstants.kLeftTurretTopLength - 1)),
    TOP_RIGHT_TURRET(
        new Section(
            LedConstants.kLeftTurretBottomLength + LedConstants.kLeftTurretTopLength,
            LedConstants.kLeftTurretBottomLength
                + LedConstants.kLeftTurretTopLength
                + LedConstants.kRightTurretTopLength
                - 1)),
    BOTTOM_RIGHT_TURRET(
        new Section(
            LedConstants.kLeftTurretBottomLength
                + LedConstants.kLeftTurretTopLength
                + LedConstants.kRightTurretTopLength,
            LedConstants.kFullLength));

    private final Section section;
/**
 * sets the section of the leds
 * @param section section of the leds that turn on
 */
    private LedSection(Section section) {
      this.section = section;
    }
/**
 * gets the section
 * @return returns the section 
 */
    public Section getSection() {
      return section;
    }
  }

  private Leds() {
    leds.setLength(buffer.getLength());
    leds.setData(buffer);
    leds.start();
  }

  @Override
  public void periodic() {
    if (RobotState.isAutonomous()) {//sets auto led color
      solid(LedSection.ALL, Color.kOrange);
    } else if (RobotState.isDisabled()) {//sets off led color
      breath(LedSection.ALL, Color.kRed, Color.kBlack, 3);
    } else { //sets teleop color
      solid(LedSection.ALL, Color.kAqua);
    }
    // solid(LedSection.TOP_LEFT_TURRET, Color.kLimeGreen);
    // solid(LedSection.BOTTOM_LEFT_TURRET, Color.kYellow);
    // solid(LedSection.BOTTOM_RIGHT_TURRET, Color.kSkyBlue);
    leds.setData(buffer); // sets buffer
  }
  /**
   * cycles through different solid colors if i am correct i could be wrong
   * @param section the section of which the leds are in
   * @param color sets the input color for the time which it sets color
   */
  public void solid(LedSection section, Color color) {
    Section s = section.getSection();
    for (int i = s.start(); i < s.end(); i++) {
      buffer.setLED(i, color);
    }
  }
/**
 * stobes colors
 * @param section sets the section of the leds
 * @param c1 sets the first color of the strobe
 * @param c2 sets the second color of the strob
 * @param duration sets how long strobes
 */
  public void strobe(LedSection section, Color c1, Color c2, double duration) {
    boolean useFirst = ((Timer.getTimestamp() % duration) / duration) > 0.5;
    solid(section, useFirst ? c1 : c2);
  }
/**
 * makes coloros breath (fade in and out sinewavy like yk)
 * @param section sets the section of the leds it goes
 * @param c1 sets the first color of it
 * @param c2 sets the second color of the breathe
 * @param duration sets the time it breathes
 */
  public void breath(LedSection section, Color c1, Color c2, double duration) {
    double x = ((Timer.getTimestamp() % duration) / duration) * 2.0 * Math.PI;
    double ratio = (Math.sin(x) + 1.0) / 2.0;

    Color mixed =
        new Color(
            c1.red * (1 - ratio) + c2.red * ratio,
            c1.green * (1 - ratio) + c2.green * ratio,
            c1.blue * (1 - ratio) + c2.blue * ratio);

    solid(section, mixed);
  }
/**
 * sets the leds to a cycling rainbow
 * @param section section of leds
 * @param cycleLength sets time between cycles
 * @param duration  sets tyme it rainbows
 */
  public void rainbow(LedSection section, double cycleLength, double duration) {
    Section s = section.getSection();
    double baseHue = (1 - ((Timer.getTimestamp() / duration) % 1.0)) * 180.0;
    double huePerLed = 180.0 / cycleLength;

    for (int i = s.start(); i < s.end(); i++) {
      int hue = (int) ((baseHue + huePerLed * (i - s.start())) % 180);
      buffer.setHSV(i, hue, 255, 255);
    }
  }
/**
 * makes the colors wave
 * @param section section of leds used
 * @param c1 first color of wave
 * @param c2 second color of wave
 * @param cycleLength length of a wave
 * @param duration duration of waving
 */
  public void wave(LedSection section, Color c1, Color c2, double cycleLength, double duration) {
    Section s = section.getSection();
    double x = (1 - ((Timer.getTimestamp() % duration) / duration)) * 2.0 * Math.PI;
    double xDiff = (2.0 * Math.PI) / cycleLength;

    for (int i = s.start(); i < s.end(); i++) {
      double ratio = (Math.pow(Math.sin(x), LedConstants.kWaveExponent) + 1.0) / 2.0;

      Color mixed =
          new Color(
              c1.red * (1 - ratio) + c2.red * ratio,
              c1.green * (1 - ratio) + c2.green * ratio,
              c1.blue * (1 - ratio) + c2.blue * ratio);

      buffer.setLED(i, mixed);
      x += xDiff;
    }
  }
  /**
   * makes stripes for the leds
   * @param section section of leds being used
   * @param colors list of stripe colors
   * @param stripeLength how long stripes are
   * @param duration time it is striping
   */
  public void stripes(LedSection section, List<Color> colors, int stripeLength, double duration) {
    Section s = section.getSection();
    int offset =
        (int) ((Timer.getTimestamp() % duration) / duration * stripeLength * colors.size());

    for (int i = s.start(); i < s.end(); i++) {
      int index =
          (int) (Math.floor((double) (i - offset) / stripeLength) + colors.size()) % colors.size();
      buffer.setLED(i, colors.get(index));
    }
  }
}
