package yf.storage.common;

public enum BacketsTypesEnum {
    PROFILES("yf-profiles"),
    SETS("yf-sets"),
    IDS("yf-ids");

    private final String name;

   private BacketsTypesEnum(final String name) {
       this.name = name;
   }

   private String getName() {
       return name;
   }

    @Override
    public String toString() {
        return getName();
    }
}
