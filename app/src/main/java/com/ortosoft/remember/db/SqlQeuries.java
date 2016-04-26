package com.ortosoft.remember.db;

/**
 * Created by dima on 26.04.2016.
 */
public final class SqlQeuries {

    // Выбирает всех людей для молитвенного правила
    public static String SelectMembersForWorship =
            " select wm.id_worship, wm.by_ord, m.* from worships_members wm" +
            " left join members m on m._id = wm.id_member" +
            " where wm.id_worship = ?" +
            " order by wm.by_ord";

    // Выбирает молитвы для молитвенного правила
    public static String SelectPrayersForWorship = "select id_prayer from prayers_worships pw where pw.id_worship = ? order by by_ord";

    // Выбирает молитвы для молитвенного правила и заданного языка. !!! Перед использованием необходимо отформатировать через String.format
    public static String SelectPrayersByLanguage =
            " select pw.id_prayer, pr.name, pr.body, pr.comment from prayers_worships pw" +
            " left join prayers_%1$s pr on pr._id = pw.id_prayer" +
            " where pw.id_worship = ? order by by_ord";
}
