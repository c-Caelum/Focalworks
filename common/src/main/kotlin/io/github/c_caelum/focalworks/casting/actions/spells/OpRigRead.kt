package io.github.c_caelum.focalworks.casting.actions.spells

import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.item.IotaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.asTextComponent
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.network.chat.Component
import at.petrak.hexcasting.api.utils.putTag
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.nbt.Tag
import net.minecraft.world.item.TooltipFlag


object OpRigRead : SpellAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
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
        val tag = hex.serialize();
        handStack.putTag("riggedread",tag)

        return SpellAction.Result(
            Spell(tag),
            (0.1 * MediaConstants.DUST_UNIT).toLong(),
            listOf()
        )
    }

    private data class Spell(val tag : Tag) : RenderedSpell {

        // IMPORTANT: do not throw mishaps in this method! mishaps should ONLY be thrown in SpellAction.execute
        override fun cast(env: CastingEnvironment) {

        }
    }
}
