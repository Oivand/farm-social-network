package com.example.kolos.interfaces;

import com.example.kolos.model.Sector;
import java.util.List;

public interface SectorInterface {

    List<Sector> findSectorByName(String nameSector);
}
