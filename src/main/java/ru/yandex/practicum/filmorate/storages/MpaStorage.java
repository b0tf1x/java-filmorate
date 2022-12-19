package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MpaStorage {
    Collection<MPA> findAll();

    MPA getById(int id);
}

