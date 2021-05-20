package com.seamfix.features.users.userdetail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.seamfix.common.NetworkChecker
import com.seamfix.core.model.table.User
import com.seamfix.features.R
import com.seamfix.features.users.UIState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.user_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.user_detail_has_data.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailBottomSheet(var user: User) : BottomSheetDialogFragment() {

    /*** ViewModel to be Injected by Hilt ***/
    private val viewModel: UserDetailViewModel by viewModels()

    companion object{
        /***  A static variable that will be true if any instance of this class is visible ***/
        var isSheetOpen:Boolean = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_detail_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //needed to remove the white background color of the bottom sheet.
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isSheetOpen = true

        //We start fetching the user:
        lifecycleScope.launch {
            getUser(user.id)
        }

        //set a listener to listen for network changes:
        viewModel.listenForNetworkChanges()
        viewModel.networkChecker.canConnect.observe(viewLifecycleOwner, Observer {
            if(it){
                setUpUIState(UIState.NETWORK_CONNECTION_AVAILABLE)
            }else{
                setUpUIState(UIState.NO_NETWORK_CONNECTION)
            }
        })
    }


    private suspend fun getUser(userID: String){
        setUpUIState(UIState.LOADING)

        val user = viewModel.getUser(userID)
        if(user != null){
            setUpUIState(UIState.DATA_FOUND,user)
        }else{
            setUpUIState(UIState.NO_DATA)
        }
    }


    /*** This method controls the UI of the BottomSheet ***/
    private fun setUpUIState(uiState: UIState, user: User? = null){
        when(uiState){
            UIState.LOADING ->{//determines UI state when data is loading:
                view_no_data?.visibility = View.VISIBLE
                view_has_data?.visibility = View.GONE
                tvError?.visibility = View.GONE
                tvNoInternetConnection?.visibility = View.GONE
            }
            UIState.DATA_FOUND ->{//determines UI state when data is found:
                view_no_data?.visibility = View.GONE
                view_has_data?.visibility = View.VISIBLE
                tvError?.visibility = View.GONE
                tvNoInternetConnection?.visibility = View.GONE

                //bind the data to the views:
                view_has_data?.name?.text =
                        requireContext().
                        getString(R.string.user_full_name, user?.title, user?.firstName, user?.lastName);
                view_has_data?.gender?.text = "${user?.gender}"
                view_has_data?.email?.text = "${user?.email}"
                view_has_data?.dob?.text = "${user?.dateOfBirth}"
                view_has_data?.reg?.text = "${user?.registerDate}"
                view_has_data?.location?.text = "${user?.location?.toString()}"

                //load the profile photo
                Picasso.get().load(user?.picture).placeholder(R.drawable.person)
                        .error(R.drawable.ic_broken_image).into(view_has_data?.ivPhoto)

                //Now, there is a case we have to handle: If there is no network when this bottom sheet
                //opens, and this is the first time the bottom sheet is opened for a particular user,
                //it means that some of the user's details will not be updated because the device
                //is now offline (details such as Gender, Birthday, Location). In this case,
                //we need to hide the parent layout displaying gender, birthday and location so that
                //the user does not see null values.
                if(user != null && user.isUserInformationComplete()){
                    view_has_data?.layout_more_details?.visibility =  View.VISIBLE
                }else{
                    view_has_data?.layout_more_details?.visibility =  View.GONE
                }
            }

            UIState.NO_DATA ->{//determines UI state when there is no data to show:
                view_no_data?.visibility = View.GONE
                view_has_data?.visibility = View.GONE
                tvError?.visibility = View.VISIBLE
                tvNoInternetConnection?.visibility = View.GONE
            }

            UIState.NO_NETWORK_CONNECTION ->{
                //determines the UI state when there is no network:
                tvNoInternetConnection?.visibility = View.VISIBLE
            }

            UIState.NETWORK_CONNECTION_AVAILABLE ->{
                //determines the UI state when there is no network:
                tvNoInternetConnection?.visibility = View.GONE
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isSheetOpen = false
    }
}