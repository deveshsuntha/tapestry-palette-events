package com.devesh.tapestry.mixins;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "PaletteObserve.js")
public class PaletteObserve
{
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String event;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String clientEvent;

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String zone;

    @Parameter
    private Object context;

    @Inject
    private ComponentResources resources;

    @InjectContainer
    private ClientElement container;

    @Inject
    private JavaScriptSupport jss;

    @Inject
    private Request request;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    void afterRender()
    {
        String eventUrl = resources.createEventLink("observe", event, context).toURI();
        JSONObject spec = new JSONObject(
            "url", eventUrl,
            "event", getClientEvent(),
            "id", container.getClientId(),
            "zone", zone
            );
        jss.addInitializerCall("observe", spec);
    }

    Object onObserve(String event, String context)
    {
        List<String> contextValues = new ArrayList<String>();
        contextValues.add(request.getParameter("selected"));
        CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
        EventContext eventContext = new StringEventContext(contextValues, valueEncoderSource);
        resources.triggerContextEvent(event, eventContext, callback);
        return callback.getResult();
    }

    String getClientEvent()
    {
        return clientEvent != null ? clientEvent : event;
    }

    public class StringEventContext implements EventContext {
        private final List<String> strings;
        private final ValueEncoderSource valueEncoderSource;
        public StringEventContext(List<String> strings,
              ValueEncoderSource valueEncoderSource) {
           super();
           this.strings = strings;
           this.valueEncoderSource = valueEncoderSource;
        }

        /**
         * Lookup a {@link ValueEncoder} and convert the string to an object.
         */
        public <T> T get(Class<T> type, int index) {
           ValueEncoder<T> encoder = valueEncoderSource.getValueEncoder(type);
           return encoder.toValue(strings.get(index));
        }

        public int getCount() {
           return strings.size();
        }

        public String[] toStrings() {
           return (String[]) strings.toArray(new String[strings.size()]);
        }
     }
}