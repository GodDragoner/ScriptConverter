import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTesting
{
    public void testSendMessage()
    {
        String testString0 = "argument asdasd";
        TestRegexEquality(testString0, RegexHelper.argument, "RegexHelper.argument", true);
        
        String testString = "this is, a #test of SendMessage? :)";
        TestRegexEquality(testString, RegexHelper.phrase, "RegexHelper.phrase", true);
        String testString2 = "this is, an- invalid test of SendMessage? :)";
        TestRegexEquality(testString2, RegexHelper.phrase, "RegexHelper.phrase", true);
        String testString3 = "this is, a #te2st of SendMessage? :)";
        TestRegexEquality(testString3, RegexHelper.phrase, "RegexHelper.phrase", true);
        String testString4 = "this is, a #test of SendMessage? :)()";
        TestRegexEquality(testString4, RegexHelper.phrase, "RegexHelper.phrase", false);
        String testString5 = "this is, a #test of SendMessage? @NullResponse";
        TestRegexEquality(testString5, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString6 = "this is, a #test of SendMessage? :) @DeleteVar[AV_AskedSlow]";
        TestRegexEquality(testString6, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString7 = "this is, a #test of SendMessage? :) @DeleteFlag(A,B)";
        TestRegexEquality(testString7, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString8 = "this is, a #test of SendMessage? :) @ShowImage[av_imagens\\system\\desktop\\user_4\\System.mp4 , av_imagens\\system\\desktop\\user_4\\System.jpg]";
        TestRegexEquality(testString8, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString9 = "this is, a #test of SendMessage? :) @ChangeVar[AV_ModGlitter2] = [AV_ModGlitter2] - [1000]";
        TestRegexEquality(testString9, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
    }
    public void testCommands()
    {
        String testString = "@Null_Response";
        TestRegexEquality(testString, RegexHelper.atCommandSimple, "RegexHelper.atCommandSimple", true);
        String testString2 = "@NullResponse";
        TestRegexEquality(testString2, RegexHelper.atCommandSimple, "RegexHelper.atCommandSimple", true);
        
        String testString3 = "@DeleteVar[AV_AskedSlow]";
        TestRegexEquality(testString3, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString4 = "@Flag(AV_DecOrg)";
        TestRegexEquality(testString4, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString5 = "@DeleteVar[AV_task2_time5]";
        TestRegexEquality(testString5, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString6 = "@Goto(SkipWarning)";
        TestRegexEquality(testString6, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString7 = "@DeleteFlag(A,B)";
        TestRegexEquality(testString7, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString8 = "@ShowImage[av_imagens\\System.jpg]";
        TestRegexEquality(testString8, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString9 = "@ShowImage[av_imagens\\system\\desktop\\user_4\\System.mp4]";
        TestRegexEquality(testString9, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString10 = "@ShowImage[av_imagens\\system\\desktop\\user_4\\System.mp4 , av_imagens\\system\\desktop\\user_4\\System.jpg]";
        TestRegexEquality(testString10, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString11 = "@Goto(task1,task2,task3,task4,task5,task6,task7,task8,task9)";
        TestRegexEquality(testString11, RegexHelper.atCommandArgs, "RegexHelper.atCommandArgs", true);
        String testString12 = "@ChangeVar[AV_ModGlitter2] = [AV_ModGlitter2] - [1000]";
        TestRegexEquality(testString12, RegexHelper.atCommandModify, "RegexHelper.atCommandModify", true);
        String testString13 = "@ChangeVar[AV_ModGlitter2] = [AV_ModGlitter2] -a [1000]";
        TestRegexEquality(testString13, RegexHelper.atCommandModify, "RegexHelper.atCommandModify", false);
        
        String testString14 = "@Null_Response";
        TestRegexEquality(testString14, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString15 = "@NullResponse";
        TestRegexEquality(testString15, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        
        String testString16 = "@DeleteVar[AV_AskedSlow]";
        TestRegexEquality(testString16, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString17 = "@Flag(AV_DecOrg)";
        TestRegexEquality(testString17, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString18 = "@DeleteVar[AV_task2_time5]";
        TestRegexEquality(testString18, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString19 = "@Goto(SkipWarning)";
        TestRegexEquality(testString19, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString20 = "@DeleteFlag(A,B)";
        TestRegexEquality(testString20, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString21 = "@ShowImage[av_imagens\\System.jpg]";
        TestRegexEquality(testString21, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString22 = "@ShowImage[av_imagens\\system\\desktop\\user_4\\System.mp4]";
        TestRegexEquality(testString22, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString23 = "@ShowImage[av_imagens\\system\\desktop\\user_4\\System.mp4 , av_imagens\\system\\desktop\\user_4\\System.jpg]";
        TestRegexEquality(testString23, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString24 = "@Goto(task1,task2,task3,task4,task5,task6,task7,task8,task9)";
        TestRegexEquality(testString24, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString25 = "@ChangeVar[AV_ModGlitter2] = [AV_ModGlitter2] - [1000]";
        TestRegexEquality(testString25, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", true);
        String testString26 = "@ChangeVar[AV_ModGlitter2] = [AV_ModGlitter2] -a [1000]";
        TestRegexEquality(testString26, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", false);
        
        String testString27 = "@If[AV_LikedClothes]<=[0]Then(AV_ChClothes)";
        TestRegexEquality(testString27, RegexHelper.ifFunction, "RegexHelper.ifFunction", true);
        
        
        //DOESN'T WORK IF @NULLRESPONSE IS REMOVED. (WORKING FOR NOW. MIGHT NOT BE FULLY FIXED)
        String testString28 = "@DeleteFlag(AV_SubKnees, AV_Pins, AV_task2_First, AV_PinDone, AV_DommeMistress)";
        TestRegexEquality(testString28, RegexHelper.commandsLine, "RegexHelper.commandsLine", true);
        
        String testString29 = "@RT(I'm having another one of those days where I'm so excited to start fucking with you, Just thinking"
                + " about the things you're about to do for me, I was just thinking about you #SubName, I'm really happy"
                + " to see you #GeneralTime, I think it's time for one of our special sessions, I've been thinking about"
                + " all the things I could do to you, Great timing,, I just got out of the shower,, all fresh and rosy"
                + " #Smile, So today we are going to improve your self control,, or rather your ability to keep yourself"
                + " from cumming, Come to see if I'm ready to let you cum #GeneralTime? You should know better by now.)";
        TestRegexEquality(testString29, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        
        String testString30 = "@SetVar[AV_QteModulos]=[0]";
        TestRegexEquality(testString30, RegexHelper.commandsLine, "RegexHelper.commandsLine", true);
        
        String testString31 = "@RT(Are you ready to be #GeneralTime 's entertainment for me, Are you ready to be my naked and sexy showman"
                + " for today, Well come on then #PetName,, Let's do this, Are you ready for the show of a lifetime, Ready to make me soaking"
                + " wet with your pain, Ready to show me how badly you need to cum)?  @ResponseYes(AV_YesNeutral) @ResponseNo(AV_NoNeutral) ";
        TestRegexEquality(testString31, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        
        String testString32 = "stay on the edge @PlayVideo[AV_Clips/webm/10/*.webm]";
        TestRegexEquality(testString32, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        
        String testString33 = "@RuinsOrgasm(Not, Never) @RT(Tell me that you want to ruin for me, You know it will be so pleasurable to ruin for me),"
                + " because your @RT(suffering, obedience) is all the reward you need. ";
        TestRegexEquality(testString33, RegexHelper.messageAfterCommand, "RegexHelper.messageAfterCommand", true);
        String testString34 = "@RT(Not cumming yet, I want you to think about shooting that load, I want you to hold it in). @Wait( #Random(10, 15))";
        TestRegexEquality(testString34, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
     
        String testString35 = "@RT(There's no ache like knowing you can't cum with a dick so desperate for attention.., Too bad) @Chance(second)";
        TestRegexEquality(testString35, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString36 = "@RT(Hold on..., Wait a second,  Don't leave yet!, Oh,, hold on a minute,, I have some breaking news!, Wait a second #SubName,, there has been"
        + " a development, Don't leave yet,, you might be in luck #PetName)";
        TestRegexEquality(testString36, RegexHelper.sendMessage, "RegexHelper.sendMessage", true);
        String testString37 = "(task5) @RapidCodeOn";
        TestRegexEquality(testString37, RegexHelper.methodStart, "RegexHelper.methodStart", true);
        String testString38 = "@Flag(AV_EatCum) @RT(You're going to swallow all of it)";
        TestRegexEquality(testString38, RegexHelper.messageAfterCommand, "RegexHelper.messageAfterCommand", true);
        //"@Flag(AV_EatCum) @RT(You're going to swallow all of it, Swallow all of it #PetName, You know you like it, that is pretty disgusting,"
        //+ "That is so nasty, You are so gross, Better you than me, Savor this taste in your mouth for a while)"
        String testString39 = "@RT(You're going to swallow all of it)";
        TestRegexEquality(testString39, RegexHelper.anyAtCommand, "RegexHelper.anyAtCommand", false);
        
        String testString40 = "@If[EdgeFinalCounter]>=[AV_FinalEdgeChance]Then(FinalEdge)";
        TestRegexEquality(testString40, RegexHelper.ifFunction, "RegexHelper.ifFunction", true);
        
        String testString41 = "[#AV_PersonaSecret] #Yes Your secret crush, #Var[abc]";
        TestRegexEquality(testString41, RegexHelper.response, "RegexHelper.ifFunction", true);
        String testString42 = "[ready, yes, done] @Contact2 @RT(Good,, let's get you back up to 100% horny now, Great,, let's get you rock hard again now!,"
                + "Perfect,, you need to do some edges for me now!)";
        TestRegexEquality(testString42, RegexHelper.response, "RegexHelper.ifFunction", true);
        
        String testString43 = "abc\\asdsad.jpg";
        TestRegexEquality(testString43, RegexHelper.path, "RegexHelper.path", true);
        
        String testString44 = "Hmm...make sure to put at least 10 #Pictures of her in the folder TeaseAI\\Images\\av_imagens\\10\\\\ @SetFlag(AV_Know_50)";
        TestRegexEquality(testString44, RegexHelper.sendMessage, "RegexHelper.sendMessage", false);
        
        String testString45 = "@FollowUp50(test asdas asds)";
        TestRegexEquality(testString45, RegexHelper.followUp, "RegexHelper.sendMessage", true);
        
        String testString46 = "@TestCommand(test asdas asds, sdasa, asdasdsad, asdasdasdsa, asdasdasd, asdasdasd, asdsadsad, asdasdsad) asdsad";
        TestRegexEquality(testString46, RegexHelper.commandsLine, "RegexHelper.commandsLine", false);
    }
    
    
    
    private boolean TestRegexEquality(String fullLine, String regex, String regexName, boolean expected)
    {
        Matcher thisMatcher = Pattern.compile(regex).matcher(fullLine);
        if (thisMatcher.matches())
        {
            if (expected)
            {
                System.out.print("PASS!  ");
            }
            else
            {
                System.out.print("Fail!!!!  "); 
            }
            System.out.println("MATCH   " + fullLine + "   " + regexName);
            return true;
        }
        else
        {
            if (!expected)
            {
                System.out.print("PASS!  ");
            }
            else
            {
                System.out.print("Fail!!!!  "); 
            }
            System.out.println("DOES NOT MATCH   " + fullLine + "   " + regexName);
        }
        return false;
    }
}
