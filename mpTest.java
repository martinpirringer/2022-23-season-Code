package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "mpTest2 (Blocks to Java)")
public class mpTest extends LinearOpMode {

  private DcMotor LeftFront;
  private DcMotor RightFront;
  private DcMotor RightRear;
  private DcMotor LeftRear;

  float Js1_Y;
  float js1_X;
  int lpos_LF;
  float js1_Twist;
  double v_LF;
  double NR20_TicsPerInch;
  double v_RF;
  String l_dir;
  String LastError;
  double v_RR;
  int NR20_maxvelocity;
  double v_LR;

  /**
   * Routine to display telemetry on screen
   */
  private void teleopR_Telemetry() {
    String t_tmp;

    telemetry.addData("Status", "Running");
    t_tmp = "V: " + Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) LeftFront).getVelocity(), 2)) + " P: " + Double.parseDouble(JavaUtil.formatNumber(LeftFront.getPower(), 2)) + " C: " + Double.parseDouble(JavaUtil.formatNumber(LeftFront.getCurrentPosition(), 2)) + " T: " + Double.parseDouble(JavaUtil.formatNumber(LeftFront.getTargetPosition(), 2)) + " B: " + LeftFront.isBusy();
    telemetry.addData("LF", t_tmp);
    t_tmp = "V: " + Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) RightFront).getVelocity(), 2)) + " P: " + Double.parseDouble(JavaUtil.formatNumber(RightFront.getPower(), 2)) + " C: " + Double.parseDouble(JavaUtil.formatNumber(RightFront.getCurrentPosition(), 2)) + " T: " + Double.parseDouble(JavaUtil.formatNumber(RightFront.getTargetPosition(), 2)) + " B: " + RightFront.isBusy();
    telemetry.addData("RF", t_tmp);
    t_tmp = "V: " + Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) RightRear).getVelocity(), 2)) + " P: " + Double.parseDouble(JavaUtil.formatNumber(RightRear.getPower(), 2)) + " C: " + Double.parseDouble(JavaUtil.formatNumber(RightRear.getCurrentPosition(), 2)) + " T: " + Double.parseDouble(JavaUtil.formatNumber(RightRear.getTargetPosition(), 2)) + " B: " + RightRear.isBusy();
    telemetry.addData("RR", t_tmp);
    t_tmp = "V: " + Double.parseDouble(JavaUtil.formatNumber(((DcMotorEx) LeftRear).getVelocity(), 2)) + " P: " + Double.parseDouble(JavaUtil.formatNumber(LeftRear.getPower(), 2)) + " C: " + Double.parseDouble(JavaUtil.formatNumber(LeftRear.getCurrentPosition(), 2)) + " T: " + Double.parseDouble(JavaUtil.formatNumber(LeftRear.getTargetPosition(), 2)) + " B: " + LeftRear.isBusy();
    telemetry.addData("LR", t_tmp);
    t_tmp = "LX: " + JavaUtil.formatNumber(gamepad1.left_stick_x, 2) + " LY: " + JavaUtil.formatNumber(gamepad1.left_stick_y, 2) + " RX: " + JavaUtil.formatNumber(gamepad1.right_stick_x, 2) + " RY: " + JavaUtil.formatNumber(gamepad1.right_stick_y, 2);
    telemetry.addData("DPad", t_tmp);
    t_tmp = "DU: " + gamepad1.dpad_up + " DD: " + gamepad1.dpad_down + " DL: " + gamepad1.dpad_left + " DR: " + gamepad1.dpad_right;
    telemetry.addData("DP1", t_tmp);
    telemetry.update();
  }

  /**
   * Handle Xdrive related stuff here
   */
  private void teleop_XDrive(double maxAcceleration) {
    double scale;

    js1_X = gamepad1.left_stick_x;
    js1_Twist = -gamepad1.right_stick_x;
    Js1_Y = gamepad1.left_stick_y;
    v_LF = js1_Twist + 1;
    v_LF = v_LF + js1_X;
    v_RF = js1_Twist - Js1_Y;
    v_RF = v_RF + js1_X;
    v_RR = js1_Twist - Js1_Y;
    v_RR = v_RR - js1_X;
    v_LR = js1_Twist + Js1_Y;
    v_LR = v_LR - js1_X;
    scale = 1;
    if (Math.abs(v_LF) > scale) {
      scale = Math.abs(v_LF);
    }
    if (Math.abs(v_RF) > scale) {
      scale = Math.abs(v_RF);
    }
    if (Math.abs(v_LR) > scale) {
      scale = Math.abs(v_LR);
    }
    if (scale > 1) {
      v_LF = v_LF / scale;
      v_RF = v_RF / scale;
      v_LR = v_LR / scale;
      v_RR = v_RR / scale;
    }
    LeftFront.setPower(v_LF);
    LeftRear.setPower(v_LR);
    RightFront.setPower(v_RF);
    RightRear.setPower(v_RR);
    if (Math.abs(v_RR) > scale) {
      scale = Math.abs(v_RR);
    }
  }

  /**
   * We only go straught fwd and reverse or sideways
   * or twist and not a combination of them.
   * This is to adapt better to the grid behavior with all the obstacles
   * Furthermore teleop_XDrive is just a lasdjustmentst resort to do a
   */
  private void Xdrive_onedirection() {
    // + = Right - = Left
    js1_X = -gamepad1.left_stick_x;
    js1_Twist = -gamepad1.right_stick_x;
    // - = Fwd + = Bacl
    Js1_Y = gamepad1.left_stick_y;
    // we only go with the biggest value from the joysticks
    // We only drive if any value != 0
    // =
    if (false) {
      if (Math.abs(js1_X) > Math.abs(Js1_Y) && Math.abs(js1_X) > Math.abs(js1_Twist)) {
        // We only look ay js1_X LR depemding of sign
        Js1_Y = 0;
        js1_Twist = 0;
      } else if (Math.abs(Js1_Y) > Math.abs(js1_Twist)) {
        // We only look ay js1_Y FB depemding on Sign
        js1_X = 0;
        js1_Twist = 0;
      } else {
        // We only look ay js1_twist
        js1_X = 0;
        Js1_Y = 0;
      }
      // We still can add the values but do not need to scale as only 1 value is between -1 and +1
      v_LF = js1_Twist + Js1_Y;
      v_LF = v_LF + js1_X;
      v_RF = js1_Twist - Js1_Y;
      v_RF = v_RF + js1_X;
      v_RR = js1_Twist - Js1_Y;
      v_RR = v_RR - js1_X;
      v_LR = js1_Twist + Js1_Y;
      v_LR = v_LR - js1_X;
      ((DcMotorEx) LeftFront).setVelocity(v_LF * NR20_maxvelocity);
      ((DcMotorEx) RightFront).setVelocity(v_RF * NR20_maxvelocity);
      ((DcMotorEx) RightRear).setVelocity(v_RR * NR20_maxvelocity);
      ((DcMotorEx) LeftRear).setVelocity(v_LR * NR20_maxvelocity);
    } else {
      // Set Direction to "N"
      l_dir = "N";
    }
  }

  /**
   * Describe this function...Move a direction in Inches
   * Directuion F - B - L - R
   * Distance in inches
   */
  private void MoveDirInches(String Direction, double Distance) {
    int nerror;
    int pulseDistance;

    nerror = 0;
    pulseDistance = (int) (NR20_TicsPerInch * Distance);
    StopAndZeroDriveEnc();
    if (Direction.equals("F")) {
      LeftFront.setTargetPosition(pulseDistance);
      RightFront.setTargetPosition(-pulseDistance);
      RightRear.setTargetPosition(-pulseDistance);
      LeftRear.setTargetPosition(pulseDistance);
    } else if (Direction.equals("B")) {
      LeftFront.setTargetPosition(-pulseDistance);
      RightFront.setTargetPosition(pulseDistance);
      RightRear.setTargetPosition(pulseDistance);
      LeftRear.setTargetPosition(-pulseDistance);
    } else if (Direction.equals("L")) {
      LeftFront.setTargetPosition(pulseDistance);
      RightFront.setTargetPosition(pulseDistance);
      RightRear.setTargetPosition(-pulseDistance);
      RightRear.setTargetPosition(-pulseDistance);
    } else if (Direction.equals("R")) {
      LeftFront.setTargetPosition(-pulseDistance);
      RightFront.setTargetPosition(-pulseDistance);
      RightRear.setTargetPosition(pulseDistance);
      LeftRear.setTargetPosition(pulseDistance);
    } else {
      nerror = 1;
      LastError = "MoveDirInches - Invalid command - " + Direction;
    }
    // If there is no error drive to target
    if (nerror == 0) {
      // Drive to target at 1/2 speed
      DriveToTargetPos(NR20_maxvelocity * 0.5);
    }
  }

  /**
   * Stop all motors and set encoders to 0
   */
  private void StopAndZeroDriveEnc() {
    LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    RightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    LeftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    Get_lpos();
  }

  /**
   * Get The last position used with l_dir to do error handling
   */
  private void Get_lpos() {
    lpos_LF = LeftFront.getCurrentPosition();
    lpos_LF = LeftFront.getCurrentPosition();
    lpos_LF = LeftFront.getCurrentPosition();
    lpos_LF = LeftFront.getCurrentPosition();
  }

  /**
   * Describe this function...
   */
  private void DriveToTargetPos(double v_Speed) {
    LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    RightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    LeftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    ((DcMotorEx) LeftFront).setVelocity(v_Speed);
    ((DcMotorEx) RightFront).setVelocity(v_Speed);
    ((DcMotorEx) RightRear).setVelocity(v_Speed);
    ((DcMotorEx) LeftRear).setVelocity(v_Speed);
    while (LeftFront.isBusy() || RightFront.isBusy() || RightRear.isBusy() || LeftRear.isBusy()) {
      teleopR_Telemetry();
      sleep(100);
    }
    StopAndZeroDriveEnc();
  }

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    int lpos_RF;
    int lpos_RR;
    int lpos_LR;
    int diff_LF;
    int diff_RF;
    int diff_RR;
    int diff_LR;
    ElapsedTime el_Time;
    double Last_Time;
    double Curr_Time;
    double diff_time;

    LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
    RightFront = hardwareMap.get(DcMotor.class, "RightFront");
    RightRear = hardwareMap.get(DcMotor.class, "RightRear");
    LeftRear = hardwareMap.get(DcMotor.class, "LeftRear");

    LeftFront.setDirection(DcMotorSimple.Direction.FORWARD);
    LeftRear.setDirection(DcMotorSimple.Direction.FORWARD);
    RightFront.setDirection(DcMotorSimple.Direction.FORWARD);
    RightRear.setDirection(DcMotorSimple.Direction.FORWARD);
    LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    LeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    RightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    ((DcMotorEx) LeftFront).setTargetPositionTolerance(2);
    ((DcMotorEx) RightFront).setTargetPositionTolerance(2);
    ((DcMotorEx) RightRear).setTargetPositionTolerance(2);
    ((DcMotorEx) LeftRear).setTargetPositionTolerance(2);
    StopAndZeroDriveEnc();
    v_LF = ((DcMotorEx) LeftFront).getVelocity();
    v_RF = ((DcMotorEx) RightFront).getVelocity();
    v_RR = ((DcMotorEx) RightRear).getVelocity();
    v_LR = ((DcMotorEx) LeftRear).getVelocity();
    // We are putting the definitions here so they are accessible in functions
    lpos_LF = LeftFront.getCurrentPosition();
    lpos_RF = RightFront.getCurrentPosition();
    lpos_RR = RightRear.getCurrentPosition();
    lpos_LR = LeftRear.getCurrentPosition();
    diff_LF = 0;
    diff_RF = 0;
    diff_RR = 0;
    diff_LR = 0;
    // Last Direction
    // N = None/Stopped
    // F = Fwd
    // B = Back
    // L= Left
    // R = Right
    l_dir = "N";
    // Max velocity of Neverrest 20 * 0.9 to leave room for adjustments
    NR20_maxvelocity = 2250;
    NR20_TicsPerInch = 49.3;
    // Witf fractional here to make it a double
    MoveDirInches("F", 0.1);
    telemetry.addData("Status", "Init");
    telemetry.update();
    // Put initialization blocks here.
    waitForStart();
    if (opModeIsActive()) {
      el_Time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
      el_Time.reset();
      Last_Time = el_Time.milliseconds();
      Curr_Time = el_Time.milliseconds();
      diff_time = Curr_Time - Last_Time;
      LastError = "None";
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // we are wrapping different section of this loop into functions by functionality like xdrive, arm, telemetry etc to make it easier to maintain
        // Test for all preprogrammed buttons first
        if (gamepad1.dpad_up) {
          MoveDirInches("F", 24);
        }
        if (gamepad1.dpad_down) {
          MoveDirInches("B", 24);
        }
        if (gamepad1.dpad_left) {
          MoveDirInches("L", 24);
        }
        if (gamepad1.dpad_right) {
          MoveDirInches("R", 24);
        }
        // JS Drive is last
        Xdrive_onedirection();
        teleopR_Telemetry();
      }
    }
    lpos_RR = RightRear.getCurrentPosition();
  }
}
