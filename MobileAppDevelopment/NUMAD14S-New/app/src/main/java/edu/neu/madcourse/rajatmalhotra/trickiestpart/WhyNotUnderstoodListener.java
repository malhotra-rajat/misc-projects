package edu.neu.madcourse.rajatmalhotra.trickiestpart;

import java.util.List;

import edu.neu.madcourse.rajatmalhotra.R;

import android.content.Context;

public class WhyNotUnderstoodListener implements OnNotUnderstoodListener {

	private Context context;
    private boolean retry;
    private VoiceActionExecutor executor;

    public WhyNotUnderstoodListener(Context context,
            VoiceActionExecutor executor, boolean retry)
    {
        this.context = context;
        this.executor = executor;
        this.retry = retry;
    }

    @Override
    public void notUnderstood(List<String> heard, int reason)
    {
        String prompt;
        switch (reason)
        {
            case OnNotUnderstoodListener.REASON_INACCURATE_RECOGNITION:
                prompt =
                        context.getResources().getString(
                                R.string.voiceaction_inaccurate);
                break;
            case OnNotUnderstoodListener.REASON_NOT_A_COMMAND:
                String firstMatchingWord = heard.get(0);
                String promptFormat =
                        context.getResources().getString(
                                R.string.voiceaction_not_command);
                prompt = String.format(promptFormat, firstMatchingWord);
                break;
            case OnNotUnderstoodListener.REASON_UNKNOWN:
            default:
                prompt =
                        context.getResources().getString(
                                R.string.voiceaction_unknown);
                break;
        }

        if (retry)
        {
            String retryPrompt =
                    context.getResources().getString(
                            R.string.voiceaction_retry);
            prompt = prompt + retryPrompt;
            executor.reExecute(prompt);
        } else
        {
            executor.speak(prompt);
        }
    }

}
