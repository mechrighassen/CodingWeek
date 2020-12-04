package Builder;

import Structure.FieldNote;
import Vue.VueFieldType;


public abstract class FieldNoteTypeBuilder {
    public abstract VueFieldType build(FieldNote f);
}
