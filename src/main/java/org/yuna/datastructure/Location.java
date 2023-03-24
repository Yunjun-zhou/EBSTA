package org.wsh.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Location implements Serializable {
    private double lng;
    private double lat;
}
