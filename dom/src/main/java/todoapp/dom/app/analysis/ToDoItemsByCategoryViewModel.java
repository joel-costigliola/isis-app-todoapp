/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package todoapp.dom.app.analysis;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

import todoapp.dom.module.categories.Category;
import todoapp.dom.module.categories.Subcategory;
import todoapp.dom.module.todoitem.ToDoItem;
import todoapp.dom.module.todoitem.ToDoItems;

@DomainObjectLayout(
        named="By Category",
        bookmarking = BookmarkPolicy.AS_ROOT
)
@DomainObject(
        nature = Nature.VIEW_MODEL
)
public class ToDoItemsByCategoryViewModel
        implements Comparable<ToDoItemsByCategoryViewModel> {

    //region > constructors
    public ToDoItemsByCategoryViewModel() {
    }
    public ToDoItemsByCategoryViewModel(final Category category) {
        setCategory(category);
    }
    //endregion


    //region > category (property)
    private Category category;

    @Title
    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }
    //endregion

    //region > notYetComplete (property), complete (property)
    @PropertyLayout(
        multiLine=5
    )
    public String getNotYetComplete() {
        final List<ToDoItem> notYetComplete = getItemsNotYetComplete();
        return Joiner.on(", ").join(
            Iterables.transform(subcategories(), summarizeBySubcategory(notYetComplete)));
    }

    @PropertyLayout(
        multiLine=5
    )
    public String getComplete() {
        final List<ToDoItem> completeInCategory = getItemsComplete();
        return Joiner.on(", ").join(
            Iterables.transform(subcategories(), summarizeBySubcategory(completeInCategory)));
    }

    // //////////////////////////////////////

    private Iterable<Subcategory> subcategories() {
        return Iterables.filter(Arrays.asList(Subcategory.values()), Subcategory.thoseFor(getCategory()));
    }

    private Function<Subcategory, String> summarizeBySubcategory(final Iterable<ToDoItem> itemsInCategory) {
        return new Function<Subcategory, String>() {
            @Override
            public String apply(final Subcategory subcategory) {
                return subcategory + ": " + countIn(itemsInCategory, subcategory);
            }
        };
    }

    private static int countIn(final Iterable<ToDoItem> items, final Subcategory subcategory) {
        return Iterables.size(Iterables.filter(items,
                ToDoItem.Predicates.thoseSubcategorised(subcategory)));
    }
    //endregion

    //region > getItemsNotYetComplete (collection), getItemsComplete (collection)
    /**
     * All those items {@link ToDoItems#notYetComplete() not yet complete}, for this {@link #getCategory() category}.
     */
    @CollectionLayout(
            render = RenderType.EAGERLY
    )
    public List<ToDoItem> getItemsNotYetComplete() {
        final List<ToDoItem> notYetComplete = toDoItems.notYetCompleteNoUi();
        return Lists.newArrayList(Iterables.filter(notYetComplete, ToDoItem.Predicates.thoseCategorised(getCategory())));
    }

    /**
     * All those items {@link ToDoItems#complete() complete}, for this {@link #getCategory() category}.
     */
    @CollectionLayout(
            render = RenderType.EAGERLY
    )
    public List<ToDoItem> getItemsComplete() {
        final List<ToDoItem> complete = toDoItems.completeNoUi();
        return Lists.newArrayList(Iterables.filter(complete, ToDoItem.Predicates.thoseCategorised(getCategory())));
    }

    //endregion

    //region > deleteCompleted (action)
    @ActionLayout(
            named="Delete"
    )
    public ToDoItemsByCategoryViewModel deleteCompleted() {
        for (final ToDoItem item : getItemsComplete()) {
            container.removeIfNotAlready(item);
        }
        // force reload of page
        return this;
    }

    //endregion

    //region > compareTo
    @Override
    public int compareTo(final ToDoItemsByCategoryViewModel other) {
        return ObjectContracts.compare(this, other, "category");
    }
    //endregion

    //region > injected services
    @javax.inject.Inject
    private DomainObjectContainer container;
    @javax.inject.Inject
    private ToDoItems toDoItems;
    //endregion

}
