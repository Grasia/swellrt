package org.swellrt.beta.model.js;

import java.util.ArrayList;

import org.swellrt.beta.common.SException;
import org.swellrt.beta.model.IllegalCastException;
import org.swellrt.beta.model.SHandlerFunc;
import org.swellrt.beta.model.SList;
import org.swellrt.beta.model.SMap;
import org.swellrt.beta.model.SNode;
import org.swellrt.beta.model.SText;
import org.swellrt.beta.model.SUtils;
import org.swellrt.beta.model.SVisitor;
import org.waveprotocol.wave.client.common.util.JsoView;

import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.annotations.JsIgnore;

public class SMapJs implements SMap {

	public interface Func {
		public void apply(String key, Object value);
	}

	private static native boolean isObject(JavaScriptObject jso) /*-{
		return typeof jso == "object";
	}-*/;

	private static native boolean isArray(JavaScriptObject jso) /*-{
	    return Array.isArray(jso);
	}-*/;

	private static native boolean isPrimitive(JavaScriptObject jso) /*-{

		if (typeof jso == "number" ||
			typeof jso == "boolean" ||
			typeof jso == "string") {

			return true;

			}

		return false;

	}-*/;

	private static native void iterateObject(JavaScriptObject jso, Func f) /*-{

	    for (var property in jso) {
	      if (jso.hasOwnProperty(property)) {
	       f.@org.swellrt.beta.model.js.SMapJs.Func::apply(Ljava/lang/String;Ljava/lang/Object;)(property, jso[property]);
	      }
	    }

	}-*/;

	private static native void iterateArray(JavaScriptObject jso, Func f) /*-{

	    for (var i = 0; i < jso.lenght; i++) {
	      f.@org.swellrt.beta.model.js.SMapJs.Func::apply(Ljava/lang/String;Ljava/lang/Object;)(i+"", jso[i]);
	    }

	}-*/;





	private static SNode castToSNode(JavaScriptObject object) throws IllegalCastException {
		if (object == null)
			throw new IllegalCastException("Error casting a null javascript object");

		if (isPrimitive(object)) {
			// We assume primite JS values are transparently converted to Java primitives
			return SUtils.castToSNode(object);
		} else if (isObject(object)) {
			return new SMapJs(object);
		} else if (isArray(object)) {
			// TBC
			throw new IllegalCastException("Error casting from javascript to SNode");
		}

		throw new IllegalCastException("Error casting from javascript to SNode");
	}




	private static Object castToValue(JavaScriptObject object) throws IllegalCastException {
		if (object == null)
			throw new IllegalCastException("Error casting a null javascript object");

		if (isPrimitive(object)) {
			return object;
		} else if (isObject(object)) {
			return new SMapJs(object);
		} else if (isArray(object)) {
			// TBC
			throw new IllegalCastException("Error casting from javascript to value");
		}

		throw new IllegalCastException("Error casting from javascript to value");
	}

	public static SMapJs create(JavaScriptObject jso) throws IllegalCastException {
		if (jso == null || !isObject(jso)) {
			throw new IllegalCastException("JavaScript type is not an object or it is null");
		}
		return new SMapJs(jso);

	}

	private final JavaScriptObject jso;
	private final JsoView jsv;

	protected SMapJs(JavaScriptObject jso) {
		this.jso = jso;
		this.jsv = JsoView.as(jso);
	}

	@Override
	public SNode pick(String key) {

		if (key == null)
			return null;

		try {
			return castToSNode(jsv.getJso(key));
		} catch (IllegalCastException e) {
			return null;
		}
	}

	@Override
	public SMap put(String key, SNode value) {
		throw new IllegalStateException("Wrapped Javascript SNode's can't be mutated");
	}

	@Override
	public SMap put(String key, Object object) throws IllegalCastException {
		throw new IllegalStateException("Wrapped Javascript SNode's can't be mutated");
	}

	@Override
	public void remove(String key) {
		throw new IllegalStateException("Wrapped Javascript SNode's can't be mutated");
	}

  @Override
  public void removeSafe(String key) {
    throw new IllegalStateException("Wrapped Javascript SNode's can't be mutated");
  }

	@Override
	public boolean has(String key) {
		return jsv.containsKey(key);
	}

	@Override
	public String[] keys() {

		ArrayList<String> keyList = new ArrayList<String>();

		iterateObject(this.jso, new Func() {
			@Override
			public void apply(String key, Object value) {
				keyList.add(key);
			}
		});

		return keyList.toArray(new String[keyList.size()]);
	}

	@Override
	public void clear() {
		throw new IllegalStateException("Wrapped Javascript SNode's can't be mutated");
	}

	@Override
	public boolean isEmpty() {
		return keys().length == 0;
	}

	@Override
	public int size() {
		return keys().length;
	}


  @SuppressWarnings("rawtypes")
  @JsIgnore
  @Override
  public void accept(SVisitor visitor) {
    visitor.visit(this);
  }

  //
  // -----------------------------------------------------
  //

  @Override
  public void set(String path, Object value) {
    SNode.set(this, path, value);
  }

  @Override
  public void push(String path, Object value) {
    SNode.push(this, path, value);
  }

  @Override
  public Object pop(String path) {
    return SNode.pop(this, path);
  }

  @Override
  public int length(String path) {
    return SNode.length(this, path);
  }

  @Override
  public boolean contains(String path, String property) {
    return SNode.contains(this, path, property);
  }

  @Override
  public void delete(String path) {
    SNode.delete(this, path);
  }

  @Override
  public Object get(String path) {
    return SNode.get(this, path);
  }

  @Override
  public SNode node(String path) throws SException {
    return SNode.node(this, path);
  }

  @Override
  public SMap asMap() {
    return this;
  }

  @Override
  public SList<? extends SNode> asList() {
    throw new IllegalStateException("Node is not a list");
  }

  @Override
  public String asString() {
    throw new IllegalStateException("Node is not a string");
  }

  @Override
  public double asDouble() {
    throw new IllegalStateException("Node is not a number");
  }

  @Override
  public int asInt() {
    throw new IllegalStateException("Node is not a number");
  }

  @Override
  public boolean asBoolean() {
    throw new IllegalStateException("Node is not a boolean");
  }

  @Override
  public SText asText() {
    throw new IllegalStateException("Node is not a text");
  }

  @Override
  public void addListener(SHandlerFunc h, String path) throws SException {
    throw new IllegalStateException("Local nodes don't support event listeners");
  }

  @Override
  public void listen(SHandlerFunc h) throws SException {
    throw new IllegalStateException("Local nodes don't support event listeners");
  }

  @Override
  public void removeListener(SHandlerFunc h, String path) throws SException {
    throw new IllegalStateException("Local nodes don't support event listeners");
  }

  @Override
  public void unlisten(SHandlerFunc h) throws SException {
    throw new IllegalStateException("Local nodes don't support event listeners");
  }
}
