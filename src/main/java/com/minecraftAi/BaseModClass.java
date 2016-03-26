package com.minecraftAi;

import com.minecraftAi.Interfaces.Commands.RunAiCommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by Mickey on 26/03/2016.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BaseModClass {

    @Mod.Instance
    public static BaseModClass instance;

    @EventHandler
    public void serverStart(FMLServerStartingEvent event){
        // Get's the current server instance
        MinecraftServer server = MinecraftServer.getServer();
        // Get's the servers command manager
        ICommandManager command = server.getCommandManager();
        // Cast to Server command manager to use
        ServerCommandManager manager = (ServerCommandManager) command;
        // Add the new command to the manager
        manager.registerCommand(new RunAiCommand());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){

    }

    @EventHandler
    public void init(FMLInitializationEvent event){

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }

}
