package org.jzl.android.recyclerview;

public class CA {

    private static int VIEW_TYPE_EXT_BASE_4_GENERATE_BEFORE_CONTENT = 0x40000000;
    private static int VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN = 0x50000000;
    private static int VIEW_TYPE_CONTENT_BASE_4_GENERATE_MAX = 0x60ffffff;
    private static int VIEW_TYPE_EXT_BASE_4_GENERATE_AFTER_CONTENT = 0x70000000;

    public static class ViewType {
        public static final int VIEW_TYPE_DEFAULT = contentTypeOf();
        public static final int VIEW_TYPE_EMPTY = contentTypeOf();
    }

    public static int contentTypeOf() {
        return VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN++;
    }

    public static int extTypeBeforeContentOf() {
        return VIEW_TYPE_EXT_BASE_4_GENERATE_BEFORE_CONTENT++;
    }

    public static int extTypeAfterContentOf() {
        return VIEW_TYPE_EXT_BASE_4_GENERATE_AFTER_CONTENT++;
    }

    public static boolean isContentType(int viewType) {
        return VIEW_TYPE_CONTENT_BASE_4_GENERATE_MIN <= viewType && viewType <= VIEW_TYPE_CONTENT_BASE_4_GENERATE_MAX;
    }

}
