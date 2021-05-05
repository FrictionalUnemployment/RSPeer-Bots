package org.frictional;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(names = "-muleName", description = "Mulers name")
    public String muleName;

    @Parameter(names = "-alchName", description = "Alch name")
    public String alchName;
    @Parameter(names = "-alchAmount", description = "Alching amount")
    public int alchAmount;
    @Parameter(names = "-startMulingBool", description = "True else whatever user specifies", arity = 1)
    public boolean startMulingBool;
    @Parameter(names = "-startMuleMoney", description = "Start mule money")
    public int startMuleMoney;
    @Parameter(names = "-keepMuleMoney", description = "Keep money ")
    public int keepMuleMoney;
}
