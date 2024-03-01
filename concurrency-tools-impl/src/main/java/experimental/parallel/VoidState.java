package experimental.parallel;

class VoidState {
  public static final VoidState Instance = new VoidState();

  @SuppressWarnings("SameReturnValue")
  static VoidState supplyDefault() {
    return Instance;
  }

  @SuppressWarnings("EmptyMethod")
  static void doNothing(VoidState ignored) {}

  @Override
  public String toString() {
    return "VoidState{}";
  }
}
