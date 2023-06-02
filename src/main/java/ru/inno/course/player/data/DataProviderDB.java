package ru.inno.course.player.data;

import ru.inno.course.player.model.Player;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

// научить работать с DB
public class DataProviderDB implements DataProvider{

    //Connection
    @Override
    public void save(Collection<Player> players) throws IOException {

    }

    @Override
    public Collection<Player> load() throws IOException {
        return new ArrayList<>();
    }
}
