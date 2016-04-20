package com.ortosoft.remember.db.members;

import java.io.Serializable;

/**
 * Created by dima on 29.02.2016.
 * Живет ли человек или нет
 */
public enum IsDead implements Serializable{
    unknoun, // Неизвестно
    yes, // Да, умер
    no // Нет, не умер
}
