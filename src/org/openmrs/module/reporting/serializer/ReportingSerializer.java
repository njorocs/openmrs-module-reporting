package org.openmrs.module.reporting.serializer;

import org.openmrs.module.serialization.xstream.XStreamShortSerializer;
import org.openmrs.module.serialization.xstream.mapper.CGLibMapper;
import org.openmrs.module.serialization.xstream.mapper.HibernateCollectionMapper;
import org.openmrs.serialization.SerializationException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.core.MapBackedDataHolder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;


public class ReportingSerializer extends XStreamShortSerializer {

	private static ThreadLocal<DataHolder> cache = new ThreadLocal<DataHolder>();
	
	/**
	 * @throws SerializationException
	 * @should serialize a cohort definition
	 * @should serialize an indicator that contains a persisted cohort definition
	 * @should serialize an indicator that contains an unsaved cohort definition
	 */
	public ReportingSerializer() throws SerializationException {
	    super(new XStream(new DomDriver()) {
	    	
	    	/**
	    	 * This method copied from XStreamSerializer constructor.
	    	 */
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				MapperWrapper mapper = new CGLibMapper(next);
				mapper = new HibernateCollectionMapper(mapper);
				//mapper = new IgnoreUnknownElementMapper(mapper);
				return mapper;
			}
			
	    	/**
	    	 * Override a mid-level XStream method to reuse a DataHolder cache if one is available 
	    	 */
	        public Object unmarshal(HierarchicalStreamReader reader, Object root) {
	            return unmarshal(reader, root, cache.get());
	        }
	    });

	    Mapper mapper = xstream.getMapper();
	    ConverterLookup converterLookup = xstream.getConverterLookup();

	    xstream.registerConverter(new CohortDefinitionConverter(mapper, converterLookup));
	    xstream.registerConverter(new DataSetDefinitionConverter(mapper, converterLookup));
	    xstream.registerConverter(new ReportDefinitionConverter(mapper, converterLookup));
	    xstream.registerConverter(new IndicatorConverter(mapper, converterLookup));
	    xstream.registerConverter(new DimensionConverter(mapper, converterLookup));
	}
	
	@Override
	synchronized public <T> T deserialize(String serializedObject, Class<? extends T> clazz) throws SerializationException {
		boolean cacheOwner = cache.get() == null;
		if (cacheOwner) {
			cache.set(new MapBackedDataHolder());
		}
		try {
			return super.deserialize(serializedObject, clazz);
		} finally {
			if (cacheOwner)
				cache.remove();
		}
	}

}