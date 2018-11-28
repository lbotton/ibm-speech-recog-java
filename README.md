# AGI Asterisk IBM Watson Service Speech to Text (java)


## Introduction

Asterisk Gateway Interface is an interface for adding functionality to Asterisk with many different programming languages. 

In this case, we will integrate Asterisk with IBM Watson service to transcribe audio to text.

*Execution example*

```shell
java -jar asterisk-java-watson-api.jar audio-file sim nao
```


## Source Code

```python
public class SpeechRecogClient {

private static final String URL = "https://stream.watsonplatform.net/speech-to-text/api";
private static final String KEY = ""; 

public static void main(String[] args) {
	// TODO Auto-generated method stub
	String fileName = args[0];
	String keyword = args[1];
	String counterKeyword = args[2];
	
	IamOptions options = new IamOptions.Builder()
		    .apiKey(KEY)
		    .build();

		SpeechToText speechToText = new SpeechToText(options);

		speechToText.setEndPoint(URL);
		
		try {
			  RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
			    .audio(new FileInputStream(fileName+".flac"))
			    .contentType("audio/flac")
			    .model("pt-BR_BroadbandModel")
			    .keywords(Arrays.asList(keyword, counterKeyword))
			    .keywordsThreshold((float) 0.5)
			    .maxAlternatives(3)
			    .build();

			  BaseRecognizeCallback baseRecognizeCallback =
			    new BaseRecognizeCallback() {

			      @Override
			      public void onTranscription
			        (SpeechRecognitionResults speechRecognitionResults) {
			          System.out.println(speechRecognitionResults);
			          
			          Gson gson = new Gson();
			          
			          //Converte String JSON para objeto Java
			          SpeechRecognitionResults obj = gson.fromJson(speechRecognitionResults
			        		  .toString(), SpeechRecognitionResults.class);
			          //SET VARIABLE <variablename> <value>
			          //print "SET VARIABLE \"$_\" \"$response{$_}\"\n";
			          System.out.println("SET VARIABLE transcript "+obj.getResults()
			          		  .get(0).getAlternatives().get(1).getTranscript()+"\n");
			      }

			      @Override
			      public void onDisconnected() {
			        System.exit(0);
			      }

			    };

			  speechToText.recognizeUsingWebSocket(recognizeOptions,
			                                       baseRecognizeCallback);
			} catch (FileNotFoundException e) {
			  e.printStackTrace();
			}
}
}
```
