package com.quesocololand.msvcattractions.converters;

import com.quesocololand.msvcattractions.models.Attraction;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

@Component
public class AttractionUpdateCallback implements BeforeConvertCallback<Attraction> {
    //Fields of AttractionEventListener
    //Constructors of AttractionEventListener
    //Field setters of AttractionEventListener (setters)
    //Field getters of AttractionEventListener (getters)
        //Methods of AttractionEventListener
    @Override
    public Attraction onBeforeConvert(Attraction attraction, String collection) {
            //This method is called for both insertions and updates
        attraction.preUpdate();
        return attraction;
    }
}
