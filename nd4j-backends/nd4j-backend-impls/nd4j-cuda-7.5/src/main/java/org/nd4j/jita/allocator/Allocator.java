package org.nd4j.jita.allocator;

import org.bytedeco.javacpp.Pointer;
import org.nd4j.jita.allocator.enums.AllocationStatus;
import org.nd4j.jita.allocator.enums.SyncState;
import org.nd4j.jita.allocator.impl.AllocationPoint;
import org.nd4j.jita.allocator.impl.AllocationShape;
import org.nd4j.jita.allocator.context.ExternalContext;
import org.nd4j.jita.conf.Configuration;
import org.nd4j.jita.conf.CudaEnvironment;
import org.nd4j.jita.mover.MemoryHandler;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * Allocator interface provides methods for transparent memory management
 *
 *
 * @author raver119@gmail.com
 */
public interface Allocator {

    /**
     * Consume and apply configuration passed in as argument
     *
     * @param configuration configuration bean to be applied
     */
    void applyConfiguration(Configuration configuration);


    /**
     * Set active CUDA environment
     *
     * @param environment
     */
    void setEnvironment(CudaEnvironment environment);

    /**
     * This method returns CudaContext for current thread
     *
     * @return
     */
    ExternalContext getDeviceContext();

    /**
     * This methods specifies Mover implementation to be used internally
     *
     * @param memoryHandler
     */
    void setMemoryHandler(MemoryHandler memoryHandler);

    /**
     * Returns current Allocator configuration
     *
     * @return current configuration
     */
    Configuration getConfiguration();

    /**
     * This method registers buffer within allocator instance
     */
   // Long pickupSpan(BaseCudaDataBuffer buffer, AllocationShape shape);

    /**
     * This method registers array's buffer within allocator instance
     * @param array INDArray object to be picked
     */
    Long pickupSpan(INDArray array);

    /**
     * This  method registers AllocationPoint within allocator instance
     *
     * @param point
     * @return
     */
    Long pickupSpan(AllocationPoint point);

    /**
     * This method hints allocator, that specific object was accessed on host side.
     * This includes putRow, putScalar;
     *
     * @param array
     */
    void tickHost(INDArray array);


    /**
     * This methods hints allocator, that specific object was accessed on device side.
     *
     * @param array
     */
    @Deprecated
    void tickDevice(INDArray array);


    /**
     * This method hints allocator, that specific object was released on device side
     *
     * @param array
     */
    void tackDevice(INDArray array);

    /**
     * This method notifies allocator, that specific object was changed on device side
     *
     * @param array
     */
    void tickDeviceWrite(INDArray array);

    /**
     * This method notifies allocator, that specific object was changed on host side
     *
     * @param array
     */
    void tickHostWrite(INDArray array);

    /**
     * This method returns actual device pointer valid for current object
     *
     * @param buffer
     */
    @Deprecated
    Pointer getPointer(DataBuffer buffer);

    /**
     * This method returns actual device pointer valid for specified shape of current object
     *
     * @param buffer
     * @param shape
     */
    @Deprecated
    Pointer getPointer(DataBuffer buffer, AllocationShape shape, boolean isView);


    /**
     * This method returns actual device pointer valid for specified INDArray
     */
    Pointer getPointer(INDArray array);


    /**
     * This method should be callsd to make sure that data on host side is actualized
     *
     * @param array
     */
    // TODO: to be moved into mover
    void synchronizeHostData(INDArray array);

    /**
     * This method should be calls to make sure that data on host side is actualized
     *
     * @param buffer
     */
    // TODO: to be moved into mover
    void synchronizeHostData(DataBuffer buffer);

    /**
     * This method should be callsd to make sure that data on host side is actualized.
     * However, this method only tries to lock data before synchronization.
     *
     * PLEASE NOTE: This methos is considered non-safe.
     *
     * @param buffer
     */
    // TODO: to be moved into mover
    void trySynchronizeHostData(DataBuffer buffer);

    /**
     * This method returns current host memory state
     *
     * @param array
     * @return
     */
    // TODO: to be moved into mover
    SyncState getHostMemoryState(INDArray array);

    /**
     * This method returns the number of top-level memory allocation.
     * No descendants are included in this result.
     *
     * @return number of allocated top-level memory chunks
     */
    int tableSize();


    /**
     * This method returns deviceId for specified array.
     * All values >= 0 are considered valid device IDs, all values < 0 are considered stubs.
     *
     * @param array
     * @return
     */
   // Integer getDeviceId(INDArray array);

    /**
     * This method returns deviceId for current thread
     * All values >= 0 are considered valid device IDs, all values < 0 are considered stubs.
     *
     * @return
     */
     Integer getDeviceId();

    /**
     *  This method allocates required chunk of memory
     *
     * @param requiredMemory
     */
    AllocationPoint allocateMemory(AllocationShape requiredMemory);

    /**
     * This method allocates required chunk of memory in specific location
     *
     * PLEASE NOTE: Do not use this method, unless you're 100% sure what you're doing
     *
     * @param requiredMemory
     * @param location
     */
    AllocationPoint allocateMemory(AllocationShape requiredMemory, AllocationStatus location);


    void memcpyBlocking(DataBuffer dstBuffer, jcuda.Pointer srcPointer, long length, long dstOffset);

    void memcpyAsync(DataBuffer dstBuffer, jcuda.Pointer srcPointer, long length, long dstOffset);

    void memcpy(DataBuffer dstBuffer, DataBuffer srcBuffer);
}
