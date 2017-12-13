package org.swellrt.beta.model;

import org.swellrt.beta.common.SException;

/**
 *
 * @author pablojan@gmail.com (Pablo Ojanguren)
 *
 */
public interface SObservableNode {

  public void addListener(SHandlerFunc h, String path) throws SException;

  public void listen(SHandlerFunc h) throws SException;

  public void removeListener(SHandlerFunc h, String path) throws SException;

  public void unlisten(SHandlerFunc h) throws SException;

}
