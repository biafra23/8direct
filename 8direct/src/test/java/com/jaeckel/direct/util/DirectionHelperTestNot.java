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
public class DirectionHelperTestNot {


    @Test
    public void shouldReturnResource() {

        int result = DirectionHelper.getDirectionImage("n");
        assertThat(result, equalTo(R.drawable.portal_n));

        result = DirectionHelper.getDirectionImage("ne");
        assertThat(result, equalTo(R.drawable.portal_ne));

        result = DirectionHelper.getDirectionImage("e");
        assertThat(result, equalTo(R.drawable.portal_e));

        result = DirectionHelper.getDirectionImage("se");
        assertThat(result, equalTo(R.drawable.portal_se));

        result = DirectionHelper.getDirectionImage("s");
        assertThat(result, equalTo(R.drawable.portal_s));


    }
}
