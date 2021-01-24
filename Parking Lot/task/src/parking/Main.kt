package parking

data class ParkingLot(val size: Int) {
    data class Car(val plate: String, val color: String) {
        override fun toString() = "$plate $color"
    }

    data class Spot(val spotNumber: Int, val isFree: Boolean, val car: Car?) {
        override fun toString() = "$spotNumber $car"
    }

    private val parkingData = Array(size) { Spot(it + 1, true, null) }

    init {
        if (size != 0) println("Created a parking lot with $size spots.")
    }

    fun park(car: Car) {
        val firstFree = parkingData.firstOrNull { it.isFree }
        if (firstFree != null) {
            parkingData[firstFree.spotNumber - 1] = Spot(firstFree.spotNumber, false, car)
            println(car.color + " car parked in spot " + firstFree.spotNumber + ".")
        } else
            println("Sorry, the parking lot is full.")
    }

    fun leave(spotNumber: Int) {
        if (!parkingData[spotNumber - 1].isFree) {
            parkingData[spotNumber - 1] = Spot(spotNumber, true, null)
            println("Spot $spotNumber is free.")
        } else
            println("There is no car in spot $spotNumber.")
    }

    fun spotByColor(color: String) {
        val query = parkingData.filter { it.car?.color.equals(color, ignoreCase = true) }.map { it.spotNumber }.joinToString()
        println(if (query.isNotEmpty()) query else "No cars with color $color were found.")
    }

    fun spotByRegistry(plate: String) {
        val query = parkingData.filter { it.car?.plate.equals(plate, ignoreCase = true) }.map { it.spotNumber }.joinToString()
        println(if (query.isNotEmpty()) query else "No cars with registration number $plate were found.")
    }

    fun regByColor(color: String) {
        val query = parkingData.filter { it.car?.color.equals(color, ignoreCase = true) }.map { it.car?.plate }.joinToString()
        println(if (query.isNotEmpty()) query else "No cars with color $color were found.")
    }

    override fun toString(): String {
        val str = parkingData.filter { !it.isFree }.joinToString("\n") { it.toString() }
        return when {
            size == 0 -> "Sorry, a parking lot has not been created."
            str.isEmpty() -> "Parking lot is empty."
            else -> str
        }
    }
}

fun main() {
    var parkingLot = ParkingLot(0)
    while (true) {
        val cmd = readLine()!!.split(" ")
        when {
            cmd[0] == "exit" -> return
            cmd[0] == "create" -> parkingLot = ParkingLot(cmd[1].toInt())
            parkingLot.size == 0 -> println("Sorry, a parking lot has not been created.")
            cmd[0] == "park" -> parkingLot.park(ParkingLot.Car(cmd[1], cmd[2]))
            cmd[0] == "leave" -> parkingLot.leave(cmd[1].toInt())
            cmd[0] == "spot_by_color" -> parkingLot.spotByColor(cmd[1])
            cmd[0] == "spot_by_reg" -> parkingLot.spotByRegistry(cmd[1])
            cmd[0] == "reg_by_color" -> parkingLot.regByColor(cmd[1])
            cmd[0] == "status" -> println(parkingLot)
        }
    }
}