package is.bradley.verdant.block.entity;

import is.bradley.verdant.block.RopeBlock;
import is.bradley.verdant.block.SmallBeamBlock;
import is.bradley.verdant.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

public class SmallBeamBlockEntity extends BlockEntity {
    private Boolean NORTH = false;
    private Boolean EAST = false;
    private Boolean SOUTH = false;
    private Boolean WEST = false;
    private Boolean UP = false;
    private Boolean DOWN = false;
    private Direction.Axis ROT = Direction.Axis.X;

    public SmallBeamBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_BEAM, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("North", this.NORTH);
        nbt.putBoolean("East", this.EAST);
        nbt.putBoolean("South", this.SOUTH);
        nbt.putBoolean("West", this.WEST);
        nbt.putBoolean("Up", this.UP);
        nbt.putBoolean("Down", this.DOWN);
        nbt.putInt("Rotation", axisToInt(this.ROT));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.NORTH = nbt.getBoolean("North");
        this.EAST = nbt.getBoolean("East");
        this.SOUTH = nbt.getBoolean("South");
        this.WEST = nbt.getBoolean("West");
        this.UP = nbt.getBoolean("Up");
        this.DOWN = nbt.getBoolean("Down");
        this.ROT = intToAxis(nbt.getInt("Rotation"));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public int axisToInt(Direction.Axis axis) {
        if (axis == Direction.Axis.X)
            return 1;
        else if (axis == Direction.Axis.Y)
            return 2;
        else return 3;
    }

    public Direction.Axis intToAxis(int i) {
        if (i == 1)
            return Direction.Axis.X;
        else if (i == 2)
            return Direction.Axis.Y;
        else return Direction.Axis.Z;
    }

    public ArrayList<Boolean> getDirections() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(this.NORTH);
        list.add(this.EAST);
        list.add(this.SOUTH);
        list.add(this.WEST);
        list.add(this.UP);
        list.add(this.DOWN);
        return list;
    }

    public Direction.Axis getRotation() {
        return this.ROT;
    }

    public void ropeConnectionCalculations(WorldAccess world, BlockState state, BlockPos pos) {
        boolean xAxis = state.get(SmallBeamBlock.X_AXIS);
        boolean yAxis = state.get(SmallBeamBlock.Y_AXIS);
        boolean zAxis = state.get(SmallBeamBlock.Z_AXIS);
        this.NORTH = world.getBlockState(pos.north()).getBlock() instanceof RopeBlock && !zAxis;
        this.SOUTH = world.getBlockState(pos.south()).getBlock() instanceof RopeBlock && !zAxis;
        this.EAST = world.getBlockState(pos.east()).getBlock() instanceof RopeBlock && !xAxis;
        this.WEST = world.getBlockState(pos.west()).getBlock() instanceof RopeBlock && !xAxis;
        this.UP = world.getBlockState(pos.up()).getBlock() instanceof RopeBlock && !yAxis;
        this.DOWN = world.getBlockState(pos.down()).getBlock() instanceof RopeBlock && !yAxis;
        if (xAxis)
            this.ROT = Direction.Axis.X;
        else if (yAxis)
            this.ROT = Direction.Axis.Y;
        else this.ROT = Direction.Axis.Z;
    }
}