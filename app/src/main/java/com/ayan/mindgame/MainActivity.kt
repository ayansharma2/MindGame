package com.ayan.mindgame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ayan.mindgame.databinding.MainActivityBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(),CardChanges{
    var list = arrayListOf<String>("A","B","C","D","E","F","G","H","A","B","C","D","E","F","G","H")
    private var attempts by Delegates.notNull<Int>()
    private var matches by Delegates.notNull<Int>()
    lateinit var adapter: CardsAdapter
    var selectedCard = mutableListOf<Int>()
    lateinit var binding : MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        window.statusBarColor = Color.WHITE
        initLogic()
        binding.resetGrid.setOnClickListener {
            initLogic()
        }
    }

    private fun initLogic(){
        binding.timer.visibility = View.VISIBLE
        list.shuffle()
        attempts = 0
        matches = 0
        updateScores()
        binding.recyclerView.adapter = CardsAdapter(true,list,this)
        object : CountDownTimer(6100,1000){
            override fun onTick(p0: Long) {
                binding.timer.text = (p0/1000).toString()
            }

            override fun onFinish() {
                binding.timer.visibility = View.GONE
                adapter = CardsAdapter(false,list,this@MainActivity)
                binding.recyclerView.adapter = adapter
            }

        }.start()
    }


    private fun updateScores() {
        binding.attempts.text = attempts.toString()
        binding.matches.text = matches.toString()
    }

    override fun onItemSelected(index: Int) {
        selectedCard.add(index)
        if(selectedCard.size == 2){
            if(list[selectedCard[0]] == list[selectedCard[1]]){
                matches++
                attempts++
                updateScores()
            } else {
                adapter.notifyItemChanged(selectedCard[0])
                adapter.notifyItemChanged(selectedCard[1])
                attempts++
                updateScores()
            }
            selectedCard.clear()
        }
    }
}




class CardsAdapter(private val showCards:Boolean, val cards:ArrayList<String>,val cardChanges: CardChanges) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isTextVisible = false
        holder.title.text = cards[position]

        if(showCards){
            holder.title.setTextColor(Color.BLACK)
        } else {
            holder.title.setTextColor(Color.WHITE)
            holder.parentlayout.setOnClickListener {
                if(!isTextVisible){
                    cardChanges.onItemSelected(position)
                    holder.title.setTextColor(Color.BLACK)
                }
                isTextVisible = !isTextVisible
            }
        }

    }


    override fun getItemCount(): Int {
        return cards.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title:TextView = itemView.findViewById(R.id.title)
        var parentlayout:CardView = itemView.findViewById(R.id.parent_layout)
    }
}





