package experimental.parallel;

class VoidState {
  public static final VoidState Instance = new VoidState();

  static VoidState supplyDefault() {
    return Instance;
  }

  static void doNothing(VoidState state) {}

  @Override
  public String toString() {
    return "VoidState{}";
  }
}
