package io.github.c_caelum.focalworks.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.utils.putTag
import at.petrak.hexcasting.xplat.IXplatAbstractions


object OpRigWrite : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val hex = ListIota(args.getList(0,argc))

        val (handStack) = env.getHeldItemToOperateOn {
            val dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it)
            dataHolder != null && (dataHolder.readIota(env.world) != null || dataHolder.emptyIota() != null)
        }
        // If there are no data holders that are readable, find a data holder that isn't readable
        // so that the error message is more helpful.
            ?: env.getHeldItemToOperateOn {
                val dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it)
                dataHolder != null
            } ?: throw MishapBadOffhandItem.of(null, "iota.read")

        val datumHolder = IXplatAbstractions.INSTANCE.findDataHolder(handStack)
            ?: throw MishapBadOffhandItem.of(handStack, "iota.read")
        val tag = IotaType.serialize(hex);
        handStack.putTag("riggedread",tag)


        return listOf()
    }
}
