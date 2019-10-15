package ca.fireball1725.firelib2.util;

public enum MinecraftColours {
  WHITE(0xf9fffe),
  ORANGE(0xf9801d),
  MAGENTA(0xc74ebd),
  LIGHTBLUE(0x3ab3da),
  YELLOW(0xfed83d),
  LIME(0x80c71f),
  PINK(0xf38baa),
  GREY(0x474f52),
  LIGHTGREY(0x9d9d97),
  CYAN(0x169c9c),
  PURPLE(0x8932b8),
  BLUE(0x3c44aa),
  BROWN(0x835432),
  GREEN(0x5e7c16),
  RED(0xb02e26),
  BLACK(0x1d1d21),
  ;

  private final String colourName;
  private final int colour;

  MinecraftColours(int colour) {
    this.colour = colour;
    colourName = this.name().toLowerCase();
  }

  public String getColourName() {
    return colourName;
  }

  public int getColour() {
    return this.colour;
  }
}
