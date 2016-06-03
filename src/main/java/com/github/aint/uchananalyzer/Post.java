/*
 * Copyright (c) <2016> <Oleksandr Tyshkovets>
 *
 * This file is part of uchan-analyzer.
 *
 *  uchan-analyzer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  uchan-analyzer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with uchan-analyzer.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.aint.uchananalyzer;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * @author Oleksandr Tyshkovets
 */

public @Data class Post {
    public enum Board {
        B, INT, ERO, GIF, X, A, COS, EXP, FFD, IG, LIT, MUZ, PK, PR, R, SHO, TV, UKR, VG, WAR, SVN
    }

    @NonNull private String author;
    @NonNull private Board board;
    @NonNull private String text;
    @NonNull private LocalDateTime date;
    @NonNull private String link;
    @NonNull private boolean hasImage;

}
