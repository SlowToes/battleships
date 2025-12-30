package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.ShipName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ship {

    private ShipName name;
    private int size;
    private List<Coordinate> position;
    private List<Coordinate> draftPosition;
    private int hitCount;

    public void hit() {
        hitCount++;
    }

    public boolean isSunk() {
        return hitCount == size;
    }

    public void commitDraft() {
        position.clear();
        position.addAll(draftPosition);
        draftPosition.clear();
    }
}
