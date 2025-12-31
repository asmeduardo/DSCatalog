package com.asmeduardo.dscatalog.dtos;

import java.time.Instant;

public record CustomErrorDTO (Instant timestamp, Integer status, String error, String path){
}