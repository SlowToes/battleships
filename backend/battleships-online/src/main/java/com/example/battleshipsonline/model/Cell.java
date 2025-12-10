package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.ShipName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cell {
    private boolean occupied;
    private boolean hit;
    private ShipName name;
}
