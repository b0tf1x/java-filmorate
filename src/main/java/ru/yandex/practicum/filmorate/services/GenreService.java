package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.GenreStorage;

import java.util.Collection;
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    public Collection<Genre> findAll(){
        return genreStorage.findAll();
    }
    public Genre getById(int id){
        return genreStorage.getById(id);
    }
}
