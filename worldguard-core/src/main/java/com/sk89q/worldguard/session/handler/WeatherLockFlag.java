/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldguard.session.handler;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.weather.WeatherType;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;

import javax.annotation.Nullable;

public class WeatherLockFlag extends FlagValueChangeHandler<WeatherType> {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<WeatherLockFlag> {
        @Override
        public WeatherLockFlag create(Session session) {
            return new WeatherLockFlag(session);
        }
    }

    private WeatherType initialWeather;

    public WeatherLockFlag(Session session) {
        super(session, Flags.WEATHER_LOCK);
    }

    @Override
    protected void onInitialValue(LocalPlayer player, ApplicableRegionSet set, WeatherType value) {
        if (value == null) {
            initialWeather = null;
            return;
        }
        initialWeather = value;
        player.setPlayerWeather(value);
    }

    @Override
    protected boolean onSetValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, WeatherType currentValue, WeatherType lastValue, MoveType moveType) {
        player.setPlayerWeather(currentValue);
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, WeatherType lastValue, MoveType moveType) {
        if (initialWeather != null) {
            player.setPlayerWeather(initialWeather);
        } else {
            player.resetPlayerWeather();
        }
        initialWeather = null;
        return true;
    }

}
