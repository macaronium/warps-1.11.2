package com.wetDude.warp;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/**
 * Warp chat command.
 */
public class CmdWarp extends CommandBase {
	// command usage string
    private static String s_usage = "Usage: /warp [add, remove, list] <name>\nadd also edits already existent warps.";

	// warp manger
    private WarpManager m_manager;

    public CmdWarp(WarpManager manager) {
        m_manager = manager;
    }

    @Override
    public String getName() {
        return "warp";
    }

    @Override
    public String getUsage(ICommandSender sender) {

        return s_usage;
    }

    @Override
    public List<String> getAliases() {
        return Collections.<String>emptyList();
    }

	// TODO: make it tweakable.
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		UUID playerUUID = player.getUUID(player.getGameProfile());
        if(player.isSpectator()) {
            return;
        }

        String cmd;
        String param;

        if(args.length == 0) {
            throw new WrongUsageException(s_usage);
        }

		// read cmd arguments
        if(args[0].equals("add")) {
            if(args.length < 2) {
                throw new WrongUsageException(s_usage);
            }
            cmd = "add";
            param = args[1];
        } else if(args[0].equals("remove")) {
            if(args.length < 2) {
                throw new WrongUsageException(s_usage);
            }
            cmd = "remove";
            param = args[1];
        } else if(args[0].equals("list")) {
            cmd = "list";
            param = "";
        } else {
            cmd = "tp";
            param = args[0];
        }

        if(cmd.equals("add")) // add warp
		{
            if(m_manager.m_storage.addWarp(param, player.getPosition(), player.dimension, playerUUID)) {
				sender.sendMessage(new TextComponentString("Warp " + param + " added."));
			} else {
				throw new CommandException("Warp " + param + " does not belong to you.");
			}
        } else if(cmd.equals("remove")) // remove warp
		{
            if(m_manager.m_storage.removeWarp(param, playerUUID)) {
                sender.sendMessage(new TextComponentString("Warp " + param + " removed."));
            } else {
				throw new CommandException("Warp " + param + " does not exist or is not yours.");
			}
        } else if(cmd.equals("tp")) // teleport to warp
		{
            WarpStorage.WarpInfo info = m_manager.m_storage.getWarp(param);
            if(info == null) {
                throw new CommandException("Warp " + param + " does not exist.");
            }

            sender.sendMessage(new TextComponentString("Warping to " + param + "..."));

            warpPlayer(player, info);
        } else if(cmd.equals("list")) // list warps in chat
		{
            String msg = "Available Warps:\n";
            for(WarpStorage.WarpInfo info : m_manager.m_storage.getWarpList()) {
                msg += info.name + " (Dim: " + info.dimension + ");\n";
            }

            sender.sendMessage(new TextComponentString(msg));
        } else {
            throw new WrongUsageException(s_usage);
        }
    }
	
	private void warpPlayer(EntityPlayerMP player, WarpStorage.WarpInfo info)
	{
		if(info.dimension != player.dimension) {
			player.changeDimension(info.dimension);
		}

        player.connection.setPlayerLocation(info.x,
											info.y,
											info.z,
											0,
											0);
	}
}
