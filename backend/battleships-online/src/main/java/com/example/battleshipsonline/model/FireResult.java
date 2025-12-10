package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.FireResultType;
import com.example.battleshipsonline.model.enums.ShipName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FireResult {
    private FireResultType type;
    private ShipName sunkShipName;
    private Player winner;
}
