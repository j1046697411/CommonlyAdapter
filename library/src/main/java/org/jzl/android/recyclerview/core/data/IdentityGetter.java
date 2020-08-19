package org.jzl.android.recyclerview.core.data;

import org.jzl.android.recyclerview.core.DataProvider;

public interface IdentityGetter<T> {

    long getDataId(DataProvider<T> dataProvider, int position);

}
