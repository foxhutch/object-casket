package org.fuchss.sqlconnector.impl.object;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SqlJSON extends SqlObjectImpl {

	protected String data;

	private ObjectMapper jsonMapper = SqlJSON.createOOM();

	private SqlJSON(String json) throws ConnectorException {
		super(SqlObject.Type.VARCHAR);
		this.data = json;
	}

	SqlJSON(Object obj) throws ConnectorException {
		super(SqlObject.Type.VARCHAR);
		try {
			if (obj != null) {
				this.data = this.jsonMapper.writeValueAsString(obj);
			}
		} catch (Exception e) {
			ObjectException.Error.Incompatible.build();
		}
	}

	private static ObjectMapper createOOM() {
		ObjectMapper oom = new ObjectMapper();
		oom.configure(SerializationFeature.INDENT_OUTPUT, true);
		oom.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		oom.setVisibility(oom.getSerializationConfig().getDefaultVisibilityChecker() //
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)//
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)//
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)//
				.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
		return oom;
	}

	@Override
	public <T> T get(Class<T> type, Field target) {
		if (this.data == null) {
			return null;
		}

		try {
			if (target == null) {
				return this.jsonMapper.readValue(this.data, type);
			}

			JavaType jt = this.calculateJavaType(target.getGenericType());
			return this.jsonMapper.readValue(this.data, jt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private JavaType calculateJavaType(java.lang.reflect.Type fieldType) {
		if (fieldType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fieldType;
			Class<?> base = (Class<?>) pt.getRawType();
			JavaType[] typeArgs = Arrays.asList(pt.getActualTypeArguments()).stream().map(t -> this.calculateJavaType(t)).toArray(JavaType[]::new);
			return this.jsonMapper.getTypeFactory().constructParametricType(base, typeArgs);
		}
		return this.jsonMapper.getTypeFactory().constructType(fieldType);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.data == null) {
				preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setObject(pos, this.data, java.sql.Types.VARCHAR);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObjectFromJava(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlJSON(null);
			}
			return new SqlJSON(obj);
		}

		@Override
		public SqlObjectImpl mkSqlObjectFromSQL(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlJSON(null);
			}
			if (obj instanceof String) {
				return new SqlJSON((String) obj);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}
	}

}
