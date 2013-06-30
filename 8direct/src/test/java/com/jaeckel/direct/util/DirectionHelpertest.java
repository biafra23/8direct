package com.jaeckel.direct.util;

import com.jaeckel.direct.R;
import com.jaeckel.direct.testrunner.FilteredRobolectricRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author flashmop
 * @date 30.06.13 15:30
 */
@RunWith(FilteredRobolectricRunner.class)
public class DirectionHelperTest {


    @Test
    public void shouldReturnResource() {

        int result = DirectionHelper.getDirectionImage("n");
        assertThat(result, equalTo(R.drawable.portal_n));
    }
}
