package com.example.devcommunity.login

import android.app.Activity
import android.content.Intent
import android.icu.lang.UCharacter.getAge
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.devcommunity.MainActivity
import com.example.devcommunity.R
import com.example.devcommunity.databinding.FragmentDetailBinding
import com.example.devcommunity.model.User
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var storageRef:FirebaseStorage
    lateinit var auth:FirebaseAuth
    private lateinit var viewModel:LoginViewModel
    var uri: Uri? = null
    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    var a:String=""
    var b:String=""
     var loc:String=""
     var emailAdd:String=""
    var dp:String=""
    var genders :Boolean= false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth=FirebaseAuth.getInstance()
        storageRef=FirebaseStorage.getInstance()
        emailAdd= activity?.intent?.getStringExtra("emailid").toString()
        binding.edtemail.setText(emailAdd)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireContext())

        setListeners()
        viewModel= ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        viewModel.getUser()
        getAge()
        getCollege()
        onRadioButtonClicked()
      //  getDetails()
        setDetails()

    }

    private fun getDetails() {
       viewModel.resultData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

           it.forEach { user ->
               Log.d("jan",user.email)

               emailAdd=user.email
           }

       })
    }

    private fun setDetails() {
            binding.continue1.setOnClickListener {
                binding.load.visibility=View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                val user=User().apply {
                     id=FirebaseAuth.getInstance().currentUser?.uid.toString()
                     name=binding.edtName.text.toString()
                     age=a
                    email=emailAdd
                    location=binding.edtLocation.text.toString()
                    collage=b
                    gender=genders
                    profile=dp

                }
                    viewModel.setData(user)
                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                    activity?.finish()
                },4000)

            }



    }

    fun onRadioButtonClicked() {

        binding.radio1.setOnCheckedChangeListener { radioGroup, i ->
            if (radioGroup.isChecked){
                genders = true
                Toast.makeText(requireContext(), "male", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getAge() {
        val string = arrayOf("18", "19", "20","21","22","23","24","25+")
        var adapter = ArrayAdapter(requireContext(), R.layout.drop_down, string)
        adapter.setDropDownViewResource(R.layout.drop_down)
        binding.autoText2.setAdapter(adapter)
        binding.autoText2.setOnItemClickListener { adapterView, view, i, l ->
            if (i==0){
                a=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }


            if (i==1){
                a=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }


            else{
                a=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getCollege() {
        val string = arrayOf("SSIPMT Raipur", "BIT Raipur", "Columbia Raipur", "GEC Raipur", "BIT Bhilai", "SSIPMT Bhilai","Others")
        var adapter = ArrayAdapter(requireContext(), R.layout.drop_down, string)
        adapter.setDropDownViewResource(R.layout.drop_down)
        binding.autoText1.setAdapter(adapter)
        binding.autoText1.setOnItemClickListener { adapterView, view, i, l ->
            if (i==0){
                b=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }


            if (i==1){
                b=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }


            else{
                b=adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "${adapterView.getItemAtPosition(i)}", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun setListeners(){
        binding.editProfile.setOnClickListener {
            if (binding.imageProfile.isEnabled) {
                picImage()
            }
        }
    }
    private fun picImage() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== IMAGE_REQUEST_CODE&&resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!
            binding.imageProfile.setImageURI(uri)
            uploadImageToFirebase(uri!!)
          //  viewModel.saveImageUrl(uri!!)
            //   binding.progressLayout.visibility= View.VISIBLE
        }
    }
   /* private fun checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED){
            checkGps()
        }
        else
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(ACCESS_FINE_LOCATION),100)
        }
    }

    private fun checkGps() {
        locationRequest= LocationRequest.create()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=5000
        locationRequest.fastestInterval=2000

        val builder= LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result=LocationServices.getSettingsClient(
            requireContext().applicationContext
        ).checkLocationSettings(builder.build())

        result.addOnCompleteListener{ task->

            try {

                val response=task.getResult(
                    ApiException::class.java
                )
                Log.d("anvi",response.toString())
                getUserLocation()
            }catch (e:ApiException){
                e.printStackTrace()
                when(e.statusCode){

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->try {

                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(requireActivity(),200)

                    }
                    catch (sendIntentException : IntentSender.SendIntentException){


                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{

                    }
                }
            }
        }
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED){

            return
        }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->

            val location= task.getResult()

            if (location != null)
            {
                try {

                    val geocoder= Geocoder(requireContext(), Locale.getDefault())
                    val address=geocoder.getFromLocation(location.latitude,location.longitude,1)

                    val addressLine= address?.get(0)?.getAddressLine(0)
                    Toast.makeText(requireContext(), "${addressLine}", Toast.LENGTH_SHORT).show()
                    if (addressLine != null) {
                        binding.edtLocation.setText(addressLine)
                    }


                    Log.d("anvi",addressLine.toString())
                    // openLocation(addressLocation.toString())
                }
                catch (e: IOException){

                }
            }
        }
    }

    private fun openLocation(location: String) {

        val uri= Uri.parse("geo:0, 0?q=$location")
        val intent= Intent(Intent.ACTION_VIEW,uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }*/

    private fun selectImageFromGallery() {

        val intent = Intent()
        intent.type = "image/*${FirebaseAuth.getInstance().currentUser!!.uid}"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            100
        )
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() +".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            dp=imageUrl

                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }
}