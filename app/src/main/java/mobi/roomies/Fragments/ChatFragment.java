package mobi.roomies.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mobi.roomies.Adapters.ChatAdapter;
import mobi.roomies.R;
import mobi.roomies.models.ChatItem;

import static com.google.android.gms.internal.zzs.TAG;

/*
    Created by Kevin Chung
    Fragment used to contain the chat functionality UI
    this fragment will utilize firebase to handle messaging via firebase
 */
public class ChatFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference chatDatabaseReference;

    EditText userTypedText;
    ArrayList<ChatItem> chatItemArrayList;
    RecyclerView recyclerView;



    private ChatAdapter chatAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatItemArrayList = new ArrayList<ChatItem>();
        chatAdapter = new ChatAdapter(chatItemArrayList);

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        chatDatabaseReference = database.getReference("messages");
     /*   chatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                chatAdapter.removeAllItems();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Log.d("name debug",messageSnapshot.child("name").getValue().toString());
                    String name =  messageSnapshot.child("name").getValue().toString();
                    String message =  messageSnapshot.child("text").getValue().toString();

                    Log.d("chatMessage","chat message name:"+name+"chat message message:"+message);
                    ChatItem chatMessage = new ChatItem(name,message);

                    chatItemArrayList.add(chatMessage);
                    Log.d("array list",Integer.toString(chatItemArrayList.size()));

                }
                chatAdapter.notifyItemInserted(chatItemArrayList.size() - 1);
                recyclerView.smoothScrollToPosition(chatItemArrayList.size() - 1);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.chat_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        // Inflate the layout for this fragment

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chatAdapter);

        ImageButton sendMessageButton = (ImageButton) view.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(this);
        userTypedText = (EditText)view.findViewById(R.id.chatInputText);
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // new instance constructor required to create a new fragment with arguments
    // change eventually
    public static ChatFragment newInstance(String page, String title) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        chatFragment.setArguments(args);
        return chatFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.sendMessageButton:
                Log.d("in message","sending message");
                //TODO: this section needs to be updated on the new firebase structure
                String textMessage = userTypedText.getText().toString();
                String key = chatDatabaseReference.child("messages").push().getKey();
                Map<String, Object> postValues = new HashMap<>();
                postValues.put("name","MyPhone");
                postValues.put("text",textMessage);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key,postValues);
                chatDatabaseReference.updateChildren(childUpdates);


                userTypedText.setText("");
                break;
            default:
                break;
        }
    }
}
