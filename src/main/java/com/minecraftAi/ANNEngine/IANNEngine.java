package com.minecraftAi.ANNEngine;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Mickey on 27/03/2016.
 */
public interface IANNEngine {
    void train(EntityPlayer player);
    void execute(EntityPlayer player);
}
