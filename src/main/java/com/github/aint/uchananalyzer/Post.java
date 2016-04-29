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

/**
 * @author Oleksandr Tyshkovets
 */
public class Post {
    private String topic;
    private String text;
    private long date;
    private boolean hasImage;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

}
