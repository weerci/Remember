package com.ortosoft.remember.db.members;

import java.io.Serializable;

/**
 * Created by dima on 29.02.2016.
 * Показывает крещен человек или нет
 */
public enum IsBaptized implements Serializable {
    unknoun, // Неизвестно
    yes, // Да, крещен
    no // Нет, не крещен
}
