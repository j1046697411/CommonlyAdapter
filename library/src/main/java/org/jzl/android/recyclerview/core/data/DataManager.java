package org.jzl.android.recyclerview.core.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.core.DataProvider;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.datablcok.DataBlock;
import org.jzl.lang.util.datablcok.DataBlockProvider;
import org.jzl.lang.util.datablcok.DataBlockProviders;
import org.jzl.lang.util.datablcok.DataObserver;
import org.jzl.lang.util.datablcok.DirtyAble;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class DataManager<T> implements DataProvider<T>, DataBlockProvider<T> {

    public static final int DATA_TYPE_DEFAULT = -1;

    private DataBlockProvider<T> dataBlockProvider = DataBlockProviders.dataBlockProvider();
    private DataClassifier<T> dataClassifier = (dataProvider, position) -> {
        T data = dataProvider.getData(position);
        if (data instanceof Classifiable) {
            return ((Classifiable) data).getDataType();
        }
        return DATA_TYPE_DEFAULT;
    };
    private IdentityGetter<T> identityGetter = (dataProvider, position) -> {
        T data = dataProvider.getData(position);
        if (data instanceof Identity) {
            return ((Identity) data).getDataId();
        }
        return RecyclerView.NO_ID;
    };

    @Override
    public int getDataCount() {
        return dataBlockProvider.size();
    }

    @Override
    public T getData(int position) {
        return dataBlockProvider.get(position);
    }

    @Override
    public int getDataType(int position) {
        return dataClassifier.getDataType(this, position);
    }

    @Override
    public long getDataId(int position) {
        return identityGetter.getDataId(this, position);
    }

    @Override
    public DataBlock<T> findDataBlockByPosition(int position) {
        return dataBlockProvider.findDataBlockByIndex(position);
    }

    @Override
    public int size() {
        return dataBlockProvider.size();
    }

    @Override
    public boolean isEmpty() {
        return dataBlockProvider.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return dataBlockProvider.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return dataBlockProvider.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return dataBlockProvider.toArray();
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T1> T1[] toArray(@NonNull T1[] a) {
        return dataBlockProvider.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return dataBlockProvider.add(t);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return dataBlockProvider.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return dataBlockProvider.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        return dataBlockProvider.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> c) {
        return dataBlockProvider.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return dataBlockProvider.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return dataBlockProvider.retainAll(c);
    }

    @Override
    public void clear() {
        dataBlockProvider.clear();
    }

    @Override
    public int hashCode() {
        return dataBlockProvider.hashCode();
    }

    @Override
    public T get(int index) {
        return dataBlockProvider.get(index);
    }

    @Override
    public T set(int index, T element) {
        return dataBlockProvider.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        dataBlockProvider.add(index, element);
    }

    @Override
    public T remove(int index) {
        return dataBlockProvider.remove(index);
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return dataBlockProvider.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return dataBlockProvider.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        return dataBlockProvider.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return dataBlockProvider.listIterator(index);
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return dataBlockProvider.subList(fromIndex, toIndex);
    }

    @Override
    public DataBlock<T> dataBlock(DataBlock.PositionType positionType, int blockId) {
        return dataBlockProvider.dataBlock(positionType, blockId);
    }

    @Override
    public int getDataBlockStartPosition(DataBlock<T> dataBlock) {
        return dataBlockProvider.getDataBlockStartPosition(dataBlock);
    }

    @Override
    public List<T> snapshot() {
        return dataBlockProvider.snapshot();
    }

    @Override
    public void addDataObserver(DataObserver dataObserver) {
        dataBlockProvider.addDataObserver(dataObserver);
    }

    @Override
    public void removeDataObserver(DataObserver dataObserver) {
        dataBlockProvider.removeDataObserver(dataObserver);
    }

    @Override
    public void addDirtyAble(DirtyAble dirtyAble) {
        dataBlockProvider.addDirtyAble(dirtyAble);
    }

    @Override
    public void removeDirtyAble(DirtyAble dirtyAble) {
        dataBlockProvider.removeDirtyAble(dirtyAble);
    }

    @Override
    public DataBlock<T> defaultDataBlock() {
        return dataBlockProvider.defaultDataBlock();
    }

    @Override
    public DataBlock<T> lastContentDataBlock() {
        return dataBlockProvider.lastContentDataBlock();
    }

    @SafeVarargs
    @Override
    public final void addAll(DataBlock.PositionType positionType, int blockId, T... data) {
        dataBlockProvider.addAll(positionType, blockId, data);
    }

    @Override
    public void addAll(DataBlock.PositionType positionType, int blockId, Collection<T> collection) {
        dataBlockProvider.addAll(positionType, blockId, collection);
    }

    @SafeVarargs
    @Override
    public final void addAllToContent(T... data) {
        dataBlockProvider.addAllToContent(data);
    }

    @Override
    public void addAllToContent(Collection<T> collection) {
        dataBlockProvider.addAllToContent(collection);
    }

    @Override
    public Set<DataBlock<T>> dataBlocks() {
        return dataBlockProvider.dataBlocks();
    }

    @Override
    public DataBlock<T> removeDataBlock(DataBlock.PositionType positionType, int blockId) {
        return dataBlockProvider.removeDataBlock(positionType, blockId);
    }

    @Override
    public boolean removeDataBlock(DataBlock<T> dataBlock) {
        return dataBlockProvider.removeDataBlock(dataBlock);
    }

    @Override
    public void removeDataBlockByPositionType(DataBlock.PositionType positionType) {
        dataBlockProvider.removeDataBlockByPositionType(positionType);
    }

    @Override
    public DataBlock<T> findDataBlockByIndex(int index) {
        return dataBlockProvider.findDataBlockByIndex(index);
    }

    @Override
    public void move(int fromPosition, int toPosition) {
        dataBlockProvider.move(fromPosition, toPosition);
    }

    public void setDataClassifier(DataClassifier<T> dataClassifier) {
        this.dataClassifier = ObjectUtils.get(dataClassifier, this.dataClassifier);
    }

    public void setIdentityGetter(IdentityGetter<T> identityGetter) {
        this.identityGetter = ObjectUtils.get(identityGetter, this.identityGetter);
    }
}
