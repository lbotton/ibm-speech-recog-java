package br.lb.watson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

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
				          SpeechRecognitionResults obj = gson.fromJson(speechRecognitionResults.toString(), SpeechRecognitionResults.class);
				          //SET VARIABLE <variablename> <value>
				          //print "SET VARIABLE \"$_\" \"$response{$_}\"\n";
				          System.out.println("SET VARIABLE transcript "+obj.getResults().get(0).getAlternatives().get(1).getTranscript()+"\n");
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
