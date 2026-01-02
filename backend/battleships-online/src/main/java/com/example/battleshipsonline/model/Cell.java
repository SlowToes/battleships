package com.example.battleshipsonline.model;

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
    private boolean draftOccupied;
    private boolean hit;

    public void commitDraft() {
        if (draftOccupied) {
            occupied = true;
            draftOccupied = false;
        }
    }
}
