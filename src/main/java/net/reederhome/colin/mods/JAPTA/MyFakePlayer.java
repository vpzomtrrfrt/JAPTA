package net.reederhome.colin.mods.JAPTA;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class MyFakePlayer extends FakePlayer {
    public MyFakePlayer(WorldServer world, GameProfile profile) {
        super(world, profile);
        this.playerNetServerHandler = new FakeNetServerHandler(world.getMinecraftServer(), this);
    }

    private class FakeNetServerHandler extends net.minecraft.network.NetHandlerPlayServer {
        public FakeNetServerHandler(MinecraftServer server, EntityPlayerMP playerIn) {
            super(server, new FakeNetworkManager(), playerIn);
        }
    }

    private class FakeNetworkManager extends NetworkManager {
        public FakeNetworkManager() {
            super(EnumPacketDirection.CLIENTBOUND);
        }
    }
}
