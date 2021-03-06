/*
 *
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.bean.validators.plugins;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.service.AllowableRangeValues;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class RangeAnnotations {
  private static final Logger LOG = LoggerFactory.getLogger(RangeAnnotations.class);

  private RangeAnnotations() {
    throw new UnsupportedOperationException();
  }

  public static AllowableRangeValues stringLengthRange(Size size) {
    LOG.debug("@Size detected: adding MinLength/MaxLength to field");
    return new AllowableRangeValues(minValue(size), maxValue(size));
  }

  private static String minValue(Size size) {
    return String.valueOf(Math.max(size.min(), 0));
  }

  private static String maxValue(Size size) {
    return String.valueOf(Math.max(0, Math.min(size.max(), Integer.MAX_VALUE)));
  }

  public static AllowableRangeValues allowableRange(Optional<Min> min, Optional<Max> max) {
    AllowableRangeValues myvalues = null;

    if (min.isPresent() && max.isPresent()) {
      LOG.debug("@Min+@Max detected: adding AllowableRangeValues to field ");
      myvalues = new AllowableRangeValues(
          Double.toString(min.get().value()),
          false,
          Double.toString(max.get().value()),
          false);

    } else if (min.isPresent()) {
      LOG.debug("@Min detected: adding AllowableRangeValues to field ");
      myvalues = new AllowableRangeValues(
          Double.toString(min.get().value()),
          false,
          null,
          null);

    } else if (max.isPresent()) {
      LOG.debug("@Max detected: adding AllowableRangeValues to field ");
      myvalues = new AllowableRangeValues(
          null,
          null,
          Double.toString(max.get().value()),
          false);
    }
    return myvalues;
  }
}
