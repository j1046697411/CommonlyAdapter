package org.jzl.android.recyclerview.data;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.core.EntityFactory;
import org.jzl.android.recyclerview.core.PositionType;
import org.jzl.android.recyclerview.data.model.IdAble;
import org.jzl.android.recyclerview.data.model.TypeAble;
import org.jzl.android.recyclerview.util.Logger;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DataManagerImpl<T> implements DataManager<T> {

    private static Logger LOG = Logger.logger(DataManagerImpl.class);

    private final Set<DataBlockImpl<?, T>> dataBlocks = new TreeSet<>();
    private final EntityFactory<T> entityFactory;

    private final Set<OnDataUpdateListener<T>> dataUpdateListeners = new HashSet<>();

    private boolean isDataUpdated = false;
    private final List<T> oldData = new ArrayList<>();

    public DataManagerImpl(EntityFactory<T> entityFactory) {
        this.entityFactory = ObjectUtils.requireNonNull(entityFactory, "entityFactory");
    }

    @Override
    public void bind(CommonlyAdapter<T, ?> adapter) {

    }

    @Override
    public T getData(int position) {
        return getOldData().get(position);
    }

    @Override
    public int getDataCount() {
        return getOldData().size();
    }

    @Override
    public int getItemViewType(int position) {
        T data = getData(position);
        if (data instanceof TypeAble) {
            return ((TypeAble) data).getType();
        } else {
            int offsetPosition = position;
            for (DataBlockImpl<?, T> dataBlock : this.dataBlocks) {
                int size = dataBlock.dataSource.size();
                if (offsetPosition >= size && dataBlock.display) {
                    offsetPosition -= size;
                } else {
                    return dataBlock.block;
                }
            }
            throw new IndexOutOfBoundsException("position = " + position);
        }
    }

    @Override
    public long getItemId(int position) {
        T data = getData(position);
        if (data instanceof IdAble) {
            return ((IdAble) data).getItemId();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public T createEntity(int itemType, Object data) {
        return entityFactory.createEntity(itemType, data);
    }

    @Override
    public <E> DataSource<E> dataSource(PositionType positionType, int block) {
        DataBlockImpl<E, T> dataBlock = findDataBlock(positionType, block);
        if (ObjectUtils.isNull(dataBlock)) {
            dataBlock = new DataBlockImpl<>(positionType, block, new DataSourceImpl<>(this), true);
            this.addDataBlock(dataBlock);
        }
        return dataBlock.dataSource;
    }

    @Override
    public DataBlockImpl<?, T> findDataBlock(int position) {
        int offsetPosition = position;
        for (DataBlockImpl<?, T> dataBlock : this.dataBlocks) {
            int size = dataBlock.dataSource.size();
            if (offsetPosition >= size && dataBlock.display) {
                offsetPosition -= size;
            } else {
                return dataBlock;
            }
        }
        return null;
    }

    @Override
    public void update() {
        List<T> oldData = new ArrayList<>(snapshot());
        isDataUpdated = true;
        LOG.d("update => old:" + oldData + "|" + snapshot());
        CollectionUtils.each(this.dataUpdateListeners, target -> target.onDataUpdate(oldData, snapshot()));
    }

    private void updateOldData() {
        if (isDataUpdated) {
            oldData.clear();
            for (DataBlockImpl<?, T> dataBlock : this.dataBlocks) {
                oldData.addAll(dataBlock.dataSource.getData());
            }
            isDataUpdated = false;
        }
    }

    private List<T> getOldData() {
        updateOldData();
        return oldData;
    }

    @Override
    public List<T> snapshot() {
        return Collections.unmodifiableList(getOldData());
    }

    @Override
    public void addDataUpdateListener(OnDataUpdateListener<T> dataUpdateListener) {
        this.dataUpdateListeners.add(dataUpdateListener);
    }

    public void removeDataUpdateListener(OnDataUpdateListener<T> dataUpdateListener) {
        this.dataUpdateListeners.remove(dataUpdateListener);
    }

    public <E> DataSource<E> findDataSource(PositionType positionType, int block) {
        DataBlockImpl<E, T> dataBlock = findDataBlock(positionType, block);
        if (ObjectUtils.nonNull(dataBlock)) {
            return dataBlock.dataSource;
        } else {
            return null;
        }
    }

    @SuppressWarnings("all")
    public <E> DataBlockImpl<E, T> findDataBlock(PositionType positionType, int block) {
        for (DataBlockImpl<?, T> dataBlock : this.dataBlocks) {
            if (dataBlock.block == block && dataBlock.positionType == positionType) {
                return (DataBlockImpl<E, T>) dataBlock;
            }
        }
        return null;
    }

    private void addDataBlock(DataBlockImpl<?, T> dataBlock) {
        this.dataBlocks.add(dataBlock);
    }

    public static class DataBlockImpl<T, E> implements Comparable<DataBlockImpl<T, E>>, DataBlock {
        private PositionType positionType;
        private int block;
        private boolean display;
        private DataSourceImpl<T, E> dataSource;

        public DataBlockImpl(PositionType positionType, int block, DataSourceImpl<T, E> dataSource, boolean display) {
            this.dataSource = dataSource;
            this.positionType = positionType;
            this.block = block;
            this.display = display;
        }

        @Override
        public int compareTo(DataBlockImpl<T, E> o) {
            int sortValue = Integer.compare(positionType.getSequence(), o.positionType.getSequence());
            if (sortValue != 0) {
                return sortValue;
            } else {
                return Integer.compare(block, o.block);
            }
        }

        @Override
        public PositionType getPositionType() {
            return positionType;
        }

        @Override
        public int getBlockId() {
            return block;
        }
    }

}
