package com.doublesibi.utils.calc.datecalculator.common;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by hunajini on 2017/06/22.
 */
public class SolarLunarJP {

    private DateInfoLunarSolar currentDateInfo = null;

    private static HashMap<Integer, DateInfoLunarSolar> solar2lunar;
    private static HashMap<Integer, DateInfoLunarSolar> lunar2solar;


    public SolarLunarJP() {
        if (solar2lunar == null) {
            solar2lunar = new HashMap<>();
        }

        if (lunar2solar == null) {
            lunar2solar = new HashMap<>();
        }

        initinalize();
    }

    public DateInfoLunarSolar getDateInfo() {
        if (this.currentDateInfo != null) {
            return this.currentDateInfo;
        }
        return null;
    }

    public String getRokuyoName() {
        if (this.currentDateInfo != null) {
            return DateInfoLunarSolar.ROKYO_NAME[this.currentDateInfo.rokuyoIdx];
        }
        return null;
    }

    public int getRokuyo() {
        if (this.currentDateInfo != null) {
            return this.currentDateInfo.rokuyoIdx;
        }
        return -1;
    }

    public boolean getLeap() {
        if (this.currentDateInfo != null) {
            return this.currentDateInfo.bLeap;
        }
        return false;
    }

    public int getLunar(int solardate) {
        int tmpSYmd = 0;
        int loopcnt = 0;
        int retdate = 0;
        int cntDate = solar2lunar.size();
        DateInfoLunarSolar dateInfoLunarSolar = null;

        if ((dateInfoLunarSolar = solar2lunar.get(solardate)) == null ) {
            Calendar c = Calendar.getInstance();
            c.set(solardate / 10000, solardate / 100 % 100 - 1, solardate % 100);

            while(true) {
                loopcnt++;
                c.add(Calendar.DAY_OF_MONTH, -1);
                tmpSYmd = c.get(Calendar.YEAR) * 10000 + c.get(Calendar.MONTH) * 100 + 100 + c.get(Calendar.DAY_OF_MONTH);
                if ((dateInfoLunarSolar = solar2lunar.get(tmpSYmd)) != null) {
                    retdate = dateInfoLunarSolar.valueDate + loopcnt;
                    this.currentDateInfo = new DateInfoLunarSolar(solardate, 0, retdate, dateInfoLunarSolar.bLeap);
                    break;
                }

                // not found.
                if (loopcnt >= cntDate)
                    break;
            }
        } else {
            this.currentDateInfo = dateInfoLunarSolar;
            retdate = dateInfoLunarSolar.valueDate;
        }

        return retdate;
    }

    public int getSolar(int lunardate) {
        return 0;
    }

    private void initinalize() {
        putInitDate();
    }

    private void putInitDate(int solardate, int lunardate, boolean bleap) {
        solar2lunar.put((Integer)solardate, new DateInfoLunarSolar(solardate, 0, lunardate, bleap));
        lunar2solar.put((Integer)lunardate, new DateInfoLunarSolar(lunardate, 1, solardate, bleap));

    }

    private void putInitDate() {
        putInitDate(20001226, 20001201, false);
        putInitDate(20010124, 20010101, false);
        putInitDate(20010223, 20010201, false);
        putInitDate(20010325, 20010301, false);
        putInitDate(20010424, 20010401, false);
        putInitDate(20010523, 20010401, true);
        putInitDate(20010621, 20010501, false);
        putInitDate(20010721, 20010601, false);
        putInitDate(20010819, 20010701, false);
        putInitDate(20010917, 20010801, false);
        putInitDate(20011017, 20010901, false);
        putInitDate(20011115, 20011001, false);
        putInitDate(20011215, 20011101, false);
        putInitDate(20020113, 20011201, false);
        putInitDate(20020212, 20020101, false);
        putInitDate(20020314, 20020201, false);
        putInitDate(20020413, 20020301, false);
        putInitDate(20020512, 20020401, false);
        putInitDate(20020611, 20020501, false);
        putInitDate(20020710, 20020601, false);
        putInitDate(20020809, 20020701, false);
        putInitDate(20020907, 20020801, false);
        putInitDate(20021006, 20020901, false);
        putInitDate(20021105, 20021001, false);
        putInitDate(20021204, 20021101, false);
        putInitDate(20030103, 20021201, false);
        putInitDate(20030201, 20030101, false);
        putInitDate(20030303, 20030201, false);
        putInitDate(20030402, 20030301, false);
        putInitDate(20030501, 20030401, false);
        putInitDate(20030531, 20030501, false);
        putInitDate(20030630, 20030601, false);
        putInitDate(20030729, 20030701, false);
        putInitDate(20030828, 20030801, false);
        putInitDate(20030926, 20030901, false);
        putInitDate(20031025, 20031001, false);
        putInitDate(20031124, 20031101, false);
        putInitDate(20031223, 20031201, false);
        putInitDate(20040122, 20040101, false);
        putInitDate(20040220, 20040201, false);
        putInitDate(20040321, 20040201, true);
        putInitDate(20040419, 20040301, false);
        putInitDate(20040519, 20040401, false);
        putInitDate(20040618, 20040501, false);
        putInitDate(20040717, 20040601, false);
        putInitDate(20040816, 20040701, false);
        putInitDate(20040914, 20040801, false);
        putInitDate(20041014, 20040901, false);
        putInitDate(20041112, 20041001, false);
        putInitDate(20041212, 20041101, false);
        putInitDate(20050110, 20041201, false);
        putInitDate(20050209, 20050101, false);
        putInitDate(20050310, 20050201, false);
        putInitDate(20050409, 20050301, false);
        putInitDate(20050508, 20050401, false);
        putInitDate(20050607, 20050501, false);
        putInitDate(20050706, 20050601, false);
        putInitDate(20050805, 20050701, false);
        putInitDate(20050904, 20050801, false);
        putInitDate(20051003, 20050901, false);
        putInitDate(20051102, 20051001, false);
        putInitDate(20051202, 20051101, false);
        putInitDate(20051231, 20051201, false);
        putInitDate(20060129, 20060101, false);
        putInitDate(20060228, 20060201, false);
        putInitDate(20060329, 20060301, false);
        putInitDate(20060428, 20060401, false);
        putInitDate(20060527, 20060501, false);
        putInitDate(20060626, 20060601, false);
        putInitDate(20060725, 20060701, false);
        putInitDate(20060824, 20060701, true);
        putInitDate(20060922, 20060801, false);
        putInitDate(20061022, 20060901, false);
        putInitDate(20061121, 20061001, false);
        putInitDate(20061220, 20061101, false);
        putInitDate(20070119, 20061201, false);
        putInitDate(20070218, 20070101, false);
        putInitDate(20070319, 20070201, false);
        putInitDate(20070417, 20070301, false);
        putInitDate(20070517, 20070401, false);
        putInitDate(20070615, 20070501, false);
        putInitDate(20070714, 20070601, false);
        putInitDate(20070813, 20070701, false);
        putInitDate(20070911, 20070801, false);
        putInitDate(20071011, 20070901, false);
        putInitDate(20071110, 20071001, false);
        putInitDate(20071210, 20071101, false);
        putInitDate(20080108, 20071201, false);
        putInitDate(20080207, 20080101, false);
        putInitDate(20080308, 20080201, false);
        putInitDate(20080406, 20080301, false);
        putInitDate(20080505, 20080401, false);
        putInitDate(20080604, 20080501, false);
        putInitDate(20080703, 20080601, false);
        putInitDate(20080801, 20080701, false);
        putInitDate(20080831, 20080801, false);
        putInitDate(20080929, 20080901, false);
        putInitDate(20081029, 20081001, false);
        putInitDate(20081128, 20081101, false);
        putInitDate(20081227, 20081201, false);
        putInitDate(20090126, 20090101, false);
        putInitDate(20090225, 20090201, false);
        putInitDate(20090327, 20090301, false);
        putInitDate(20090425, 20090401, false);
        putInitDate(20090524, 20090501, false);
        putInitDate(20090623, 20090501, true);
        putInitDate(20090722, 20090601, false);
        putInitDate(20090820, 20090701, false);
        putInitDate(20090919, 20090801, false);
        putInitDate(20091018, 20090901, false);
        putInitDate(20091117, 20091001, false);
        putInitDate(20091216, 20091101, false);
        putInitDate(20100115, 20091201, false);
        putInitDate(20100214, 20100101, false);
        putInitDate(20100316, 20100201, false);
        putInitDate(20100414, 20100301, false);
        putInitDate(20100514, 20100401, false);
        putInitDate(20100612, 20100501, false);
        putInitDate(20100712, 20100601, false);
        putInitDate(20100810, 20100701, false);
        putInitDate(20100908, 20100801, false);
        putInitDate(20101008, 20100901, false);
        putInitDate(20101106, 20101001, false);
        putInitDate(20101206, 20101101, false);
        putInitDate(20110104, 20101201, false);
        putInitDate(20110203, 20110101, false);
        putInitDate(20110305, 20110201, false);
        putInitDate(20110403, 20110301, false);
        putInitDate(20110503, 20110401, false);
        putInitDate(20110602, 20110501, false);
        putInitDate(20110701, 20110601, false);
        putInitDate(20110731, 20110701, false);
        putInitDate(20110829, 20110801, false);
        putInitDate(20110927, 20110901, false);
        putInitDate(20111027, 20111001, false);
        putInitDate(20111125, 20111101, false);
        putInitDate(20111225, 20111201, false);
        putInitDate(20120123, 20120101, false);
        putInitDate(20120222, 20120201, false);
        putInitDate(20120322, 20120301, false);
        putInitDate(20120421, 20120301, true);
        putInitDate(20120521, 20120401, false);
        putInitDate(20120620, 20120501, false);
        putInitDate(20120719, 20120601, false);
        putInitDate(20120818, 20120701, false);
        putInitDate(20120916, 20120801, false);
        putInitDate(20121015, 20120901, false);
        putInitDate(20121114, 20121001, false);
        putInitDate(20121213, 20121101, false);
        putInitDate(20130112, 20121201, false);
        putInitDate(20130210, 20130101, false);
        putInitDate(20130312, 20130201, false);
        putInitDate(20130410, 20130301, false);
        putInitDate(20130510, 20130401, false);
        putInitDate(20130510, 20130401, true);
        putInitDate(20130609, 20130501, false);
        putInitDate(20130708, 20130601, false);
        putInitDate(20130807, 20130701, false);
        putInitDate(20130905, 20130801, false);
        putInitDate(20131005, 20130901, false);
        putInitDate(20131103, 20131001, false);
        putInitDate(20131203, 20131101, false);
        putInitDate(20140101, 20131201, false);
        putInitDate(20140131, 20140101, false);
        putInitDate(20140301, 20140201, false);
        putInitDate(20140331, 20140301, false);
        putInitDate(20140429, 20140401, false);
        putInitDate(20140529, 20140501, false);
        putInitDate(20140627, 20140601, false);
        putInitDate(20140727, 20140701, false);
        putInitDate(20140825, 20140801, false);
        putInitDate(20140924, 20140901, false);
        putInitDate(20141024, 20140901, true);
        putInitDate(20141122, 20141001, false);
        putInitDate(20141222, 20141101, false);
        putInitDate(20150120, 20141201, false);
        putInitDate(20150219, 20150101, false);
        putInitDate(20150320, 20150201, false);
        putInitDate(20150419, 20150301, false);
        putInitDate(20150518, 20150401, false);
        putInitDate(20150616, 20150501, false);
        putInitDate(20150716, 20150601, false);
        putInitDate(20150814, 20150701, false);
        putInitDate(20150913, 20150801, false);
        putInitDate(20151013, 20150901, false);
        putInitDate(20151112, 20151001, false);
        putInitDate(20151211, 20151101, false);
        putInitDate(20160110, 20151201, false);
        putInitDate(20160208, 20160101, false);
        putInitDate(20160309, 20160201, false);
        putInitDate(20160407, 20160301, false);
        putInitDate(20160507, 20160401, false);
        putInitDate(20160605, 20160501, false);
        putInitDate(20160704, 20160601, false);
        putInitDate(20160803, 20160701, false);
        putInitDate(20160901, 20160801, false);
        putInitDate(20161001, 20160901, false);
        putInitDate(20161031, 20161001, false);
        putInitDate(20161129, 20161101, false);
        putInitDate(20161229, 20161201, false);
        putInitDate(20170128, 20170101, false);
        putInitDate(20170227, 20170201, false);
        putInitDate(20170328, 20170301, false);
        putInitDate(20170426, 20170401, false);
        putInitDate(20170526, 20170501, false);
        putInitDate(20170624, 20170501, true);
        putInitDate(20170723, 20170601, false);
        putInitDate(20170822, 20170701, false);
        putInitDate(20170920, 20170801, false);
        putInitDate(20171020, 20170901, false);
        putInitDate(20171118, 20171001, false);
        putInitDate(20171218, 20171101, false);
        putInitDate(20180117, 20171201, false);
        putInitDate(20180216, 20180101, false);
        putInitDate(20180317, 20180201, false);
        putInitDate(20180416, 20180301, false);
        putInitDate(20180515, 20180401, false);
        putInitDate(20180614, 20180501, false);
        putInitDate(20180713, 20180601, false);
        putInitDate(20180811, 20180701, false);
        putInitDate(20180910, 20180801, false);
        putInitDate(20181009, 20180901, false);
        putInitDate(20181108, 20181001, false);
        putInitDate(20181207, 20181101, false);
        putInitDate(20190106, 20181201, false);
        putInitDate(20190205, 20190101, false);
        putInitDate(20190307, 20190201, false);
        putInitDate(20190405, 20190301, false);
        putInitDate(20190505, 20190401, false);
        putInitDate(20190603, 20190501, false);
        putInitDate(20190703, 20190601, false);
        putInitDate(20190801, 20190701, false);
        putInitDate(20190830, 20190801, false);
        putInitDate(20190929, 20190901, false);
        putInitDate(20191028, 20191001, false);
        putInitDate(20191127, 20191101, false);
        putInitDate(20191226, 20191201, false);
        putInitDate(20200125, 20200101, false);
        putInitDate(20200224, 20200201, false);
        putInitDate(20200324, 20200301, false);
        putInitDate(20200423, 20200401, false);
        putInitDate(20200523, 20200401, true);
        putInitDate(20200621, 20200501, false);
        putInitDate(20200721, 20200601, false);
        putInitDate(20200819, 20200701, false);
        putInitDate(20200917, 20200801, false);
        putInitDate(20201017, 20200901, false);
        putInitDate(20201115, 20201001, false);
        putInitDate(20201215, 20201101, false);
        putInitDate(20210113, 20201201, false);
        putInitDate(20210212, 20210101, false);
        putInitDate(20210313, 20210201, false);
        putInitDate(20210412, 20210301, false);
        putInitDate(20210512, 20210401, false);
        putInitDate(20210610, 20210501, false);
        putInitDate(20210710, 20210601, false);
        putInitDate(20210808, 20210701, false);
        putInitDate(20210907, 20210801, false);
        putInitDate(20211006, 20210901, false);
        putInitDate(20211105, 20211001, false);
        putInitDate(20211204, 20211101, false);
        putInitDate(20220103, 20211201, false);
        putInitDate(20220201, 20220101, false);
        putInitDate(20220303, 20220201, false);
        putInitDate(20220401, 20220301, false);
        putInitDate(20220501, 20220401, false);
        putInitDate(20220530, 20220501, false);
        putInitDate(20220629, 20220601, false);
        putInitDate(20220729, 20220701, false);
        putInitDate(20220827, 20220801, false);
        putInitDate(20220926, 20220901, false);
        putInitDate(20221025, 20221001, false);
        putInitDate(20221124, 20221101, false);
        putInitDate(20221223, 20221201, false);
        putInitDate(20230122, 20230101, false);
        putInitDate(20230220, 20230201, false);
        putInitDate(20230322, 20230201, true);
        putInitDate(20230420, 20230301, false);
        putInitDate(20230520, 20230401, false);
        putInitDate(20230618, 20230501, false);
        putInitDate(20230718, 20230601, false);
        putInitDate(20230816, 20230701, false);
        putInitDate(20230915, 20230801, false);
        putInitDate(20231015, 20230901, false);
        putInitDate(20231113, 20231001, false);
        putInitDate(20231213, 20231101, false);
        putInitDate(20240111, 20231201, false);
        putInitDate(20240210, 20240101, false);
        putInitDate(20240310, 20240201, false);
        putInitDate(20240409, 20240301, false);
        putInitDate(20240508, 20240401, false);
        putInitDate(20240606, 20240501, false);
        putInitDate(20240706, 20240601, false);
        putInitDate(20240804, 20240701, false);
        putInitDate(20240903, 20240801, false);
        putInitDate(20241003, 20240901, false);
        putInitDate(20241101, 20241001, false);
        putInitDate(20241201, 20241101, false);
        putInitDate(20241231, 20241201, false);
        putInitDate(20250129, 20250101, false);
        putInitDate(20250228, 20250201, false);
        putInitDate(20250329, 20250301, false);
        putInitDate(20250428, 20250401, false);
        putInitDate(20250527, 20250501, false);
        putInitDate(20250625, 20250601, false);
        putInitDate(20250725, 20250601, true);
        putInitDate(20250823, 20250701, false);
        putInitDate(20250922, 20250801, false);
        putInitDate(20251021, 20250901, false);
        putInitDate(20251120, 20251001, false);
        putInitDate(20251220, 20251101, false);
        putInitDate(20260119, 20251201, false);
        putInitDate(20260217, 20260101, false);
        putInitDate(20260319, 20260201, false);
        putInitDate(20260417, 20260301, false);
        putInitDate(20260517, 20260401, false);
        putInitDate(20260615, 20260501, false);
        putInitDate(20260714, 20260601, false);
        putInitDate(20260813, 20260701, false);
        putInitDate(20260911, 20260801, false);
        putInitDate(20261011, 20260901, false);
        putInitDate(20261109, 20261001, false);
        putInitDate(20261209, 20261101, false);
        putInitDate(20270108, 20261201, false);
        putInitDate(20270207, 20270101, false);
        putInitDate(20270308, 20270201, false);
        putInitDate(20270407, 20270301, false);
        putInitDate(20270506, 20270401, false);
        putInitDate(20270605, 20270501, false);
        putInitDate(20270704, 20270601, false);
        putInitDate(20270802, 20270701, false);
        putInitDate(20270901, 20270801, false);
        putInitDate(20270930, 20270901, false);
        putInitDate(20271029, 20271001, false);
        putInitDate(20271128, 20271101, false);
        putInitDate(20271228, 20271201, false);
        putInitDate(20280127, 20280101, false);
        putInitDate(20280225, 20280201, false);
        putInitDate(20280326, 20280301, false);
        putInitDate(20280425, 20280401, false);
        putInitDate(20280524, 20280501, false);
        putInitDate(20280623, 20280501, true);
        putInitDate(20280722, 20280601, false);
        putInitDate(20280820, 20280701, false);
        putInitDate(20280919, 20280801, false);
        putInitDate(20281018, 20280901, false);
        putInitDate(20281116, 20281001, false);
        putInitDate(20281216, 20281101, false);
        putInitDate(20290115, 20281201, false);
        putInitDate(20290213, 20290101, false);
        putInitDate(20290315, 20290201, false);
        putInitDate(20290414, 20290301, false);
        putInitDate(20290513, 20290401, false);
        putInitDate(20290612, 20290501, false);
        putInitDate(20290712, 20290601, false);
        putInitDate(20290810, 20290701, false);
        putInitDate(20290908, 20290801, false);
        putInitDate(20291008, 20290901, false);
        putInitDate(20291106, 20291001, false);
        putInitDate(20291205, 20291101, false);
        putInitDate(20300104, 20291201, false);
        putInitDate(20300203, 20300101, false);
        putInitDate(20300304, 20300201, false);
        putInitDate(20300403, 20300301, false);
        putInitDate(20300502, 20300401, false);
        putInitDate(20300601, 20300501, false);
        putInitDate(20300701, 20300601, false);
        putInitDate(20300730, 20300701, false);
        putInitDate(20300829, 20300801, false);
        putInitDate(20300927, 20300901, false);
        putInitDate(20301027, 20301001, false);
        putInitDate(20301125, 20301101, false);
        putInitDate(20301225, 20301201, false);
        putInitDate(20310123, 20310101, false);
    }
}
